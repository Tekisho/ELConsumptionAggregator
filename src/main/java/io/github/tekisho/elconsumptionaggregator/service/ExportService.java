package io.github.tekisho.elconsumptionaggregator.service;

import io.github.tekisho.elconsumptionaggregator.model.ConsumptionDay;
import io.github.tekisho.elconsumptionaggregator.model.TimeSlots;
import io.github.tekisho.elconsumptionaggregator.util.StyleProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.github.tekisho.elconsumptionaggregator.service.GroupedAverageInsertionService.GROUP_SIZE;

public class ExportService {
    private static final Logger logger = LoggerFactory.getLogger(ExportService.class);
    private StyleProvider styleProvider;

    public void export(Path savePath, Path originalPath, List<ConsumptionDay> processedConsumptionDays) throws IOException
    {
        logger.info("Starting export to \"{}\"", savePath);
        String extension = originalPath.getFileName().endsWith(".xlsx") ? ".xlsx" : ".xls";

        try (Workbook originalWorkbook = WorkbookFactory.create(originalPath.toFile());
             Workbook outputWorkbook = extension.equals(".xlsx")
                     ? new XSSFWorkbook()
                     : new HSSFWorkbook()
        ) {
            styleProvider = new StyleProvider(outputWorkbook);

            Sheet sheet = outputWorkbook.createSheet("Aggregated");

            logger.debug("Writing MPRN row...");
            writeMprnRow(originalWorkbook.getSheetAt(0), sheet);

            logger.debug("Writing header row...");
            writeHeaderRow(sheet);

            logger.debug("Writing {} data rows...", processedConsumptionDays.size());
            writeDataRows(sheet, processedConsumptionDays);

            int columnCount = sheet.getRow(1).getLastCellNum();
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 4 * 256);
            }

            try (OutputStream outputStream = Files.newOutputStream(savePath)) {
                outputWorkbook.write(outputStream);
            }

            logger.info("Export completed successfully. File saved to \"{}\"", savePath);
        } catch (IOException ioe) {
            logger.error("Export failed for file: {}", savePath, ioe);
            throw ioe;
        }
    }

    private void writeMprnRow(Sheet originalSheet, Sheet sheet) {
        String mprn = originalSheet.getRow(0).getCell(0).getStringCellValue();
        Cell mprnCell = sheet.createRow(0).createCell(0);
        mprnCell.setCellValue(mprn);
        logger.debug("Copied MPRN: {}", mprn);

        mprnCell.setCellStyle(styleProvider.getStyle(StyleProvider.StyleKey.MPRN));
    }

    private void writeHeaderRow(Sheet sheet) {
        Row slotsRow = sheet.createRow(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        slotsRow.createCell(0).setCellValue("Date");
        slotsRow.getCell(0).setCellStyle(styleProvider.getStyle(StyleProvider.StyleKey.HEADER));

        List<LocalTime> timeList = new TimeSlots().getSlots();
        int colIndex = 1;
        int counter = 0;

        for (LocalTime time : timeList) {
            String label = time.equals(LocalTime.MIDNIGHT)
                    ? "24:00"
                    : time.format(dateTimeFormatter);

            Cell timeCell = slotsRow.createCell(colIndex++);
            timeCell.setCellValue(label);
            timeCell.setCellStyle(styleProvider.getStyle(StyleProvider.StyleKey.HEADER));
            counter++;

            if (counter == GROUP_SIZE) {
                Cell averageCell = slotsRow.createCell(colIndex++);
                averageCell.setCellValue("average");
                averageCell.setCellStyle(styleProvider.getStyle(StyleProvider.StyleKey.HEADER));
                counter = 0;
            }
        }

        logger.debug("Header written: {} time slots, average columns", timeList.size());
    }

    private void writeDataRows(Sheet sheet, List<ConsumptionDay> processedConsumptionDays) {
        for (int rowIndex = 0; rowIndex < processedConsumptionDays.size(); rowIndex++) {
            ConsumptionDay consumptionDay = processedConsumptionDays.get(rowIndex);
            Row row = sheet.createRow(rowIndex + 2);

            String date = consumptionDay.getDate();
            List<Double> values = consumptionDay.getValues();

            Cell dateCell = row.createCell(0);
            dateCell.setCellValue(date);
            dateCell.setCellStyle(styleProvider.getStyle(StyleProvider.StyleKey.DATA));

            for (int colIndex = 0; colIndex < values.size(); colIndex++) {
                Cell cell = row.createCell(colIndex + 1);
                cell.setCellValue(values.get(colIndex));
                cell.setCellStyle(styleProvider.getStyle(StyleProvider.StyleKey.DATA));
            }
        }

        logger.debug("Data rows written successfully: {}", processedConsumptionDays.size());
    }
}
