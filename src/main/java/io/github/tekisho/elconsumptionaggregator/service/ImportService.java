package io.github.tekisho.elconsumptionaggregator.service;

import io.github.tekisho.elconsumptionaggregator.model.ConsumptionDay;
import io.github.tekisho.elconsumptionaggregator.model.TimeSlots;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ImportService {
    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);

    private final TimeSlots expectedTimeSlots = new TimeSlots();
    private static final int SHEET_INDEX = 0;
    private static final int HEADER_ROW_INDEX = 1;
    private static final int DATA_START_ROW_INDEX = 2;
    private static final int EXPECTED_DATA_COLUMNS = 96;

    private final DataFormatter dataFormatter = new DataFormatter();

    public List<ConsumptionDay> load(File file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file)) {
            // accessing file
            logger.debug("Opening Excel file: {}", file.getAbsolutePath());

            Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
            logger.debug("Loaded sheet \"{}\"", sheet.getSheetName());

            // gen. validation
            validateNotEmpty(sheet);
            logger.debug("Sheet is not empty, proceeding with header validation...");

            validateHeaderRow(sheet.getRow(HEADER_ROW_INDEX));
            logger.debug("Header row validated successfully.");

            // mapping + rows validation
            List<ConsumptionDay> result = mapRowsToConsumptionDays(sheet);
            logger.info("Imported {} consumption days from file \"{}\"", result.size(), file.getName());

            return result;
        } catch(IOException ioe) {
            logger.error("Import failed for file: {}", file.getAbsolutePath(), ioe);
            throw ioe;
        }
    }

    private List<ConsumptionDay> mapRowsToConsumptionDays(Sheet sheet) {
        int skippedRows = 0;

        List<ConsumptionDay> days = new ArrayList<>();
        for (int rowIndex = DATA_START_ROW_INDEX; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (!isRowComplete(row, 0, EXPECTED_DATA_COLUMNS)) {
                skippedRows++;
                continue;
            }
            days.add(mapRow(row));
        }

        if (skippedRows > 0) {
            logger.info("Skipped {} incomplete rows", skippedRows);
        }

        return days;
    }

    private ConsumptionDay mapRow(Row row) {
        List<Double> values = new ArrayList<>();
        String date  = dataFormatter.formatCellValue(row.getCell(0));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");

        if (!date.matches("\\d{6}")) {
            logger.error("Row {}: Invalid date format \"{}\"", row.getRowNum(), date);
            throw new RuntimeException("Invalid date format: " + date);
        }

        try {
            LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException dateTimeParseException) {
            logger.error("Row {}: Date parsing failed for \"{}\"", row.getRowNum(), date);
            throw new RuntimeException("Invalid date value: " + date);
        }

        for (int colIndex = 1; colIndex <= EXPECTED_DATA_COLUMNS; colIndex++) {
            Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null || cell.getCellType() != CellType.NUMERIC) {
                logger.error("Row {}: Invalid numeric data at column {}", row.getRowNum() + 1, colIndex + 1);
                throw new RuntimeException("Missing or invalid numeric data at row " + row.getRowNum() + ", column " + colIndex);
            }

            values.add(cell.getNumericCellValue());
        }

        return new ConsumptionDay(date, values);
    }

    private boolean isRowComplete(Row row, int dataStartCol, int dataEndCol) {
        if (row == null) {
            return false;
        }

        for (int colIndex = dataStartCol; colIndex <= dataEndCol; colIndex++) {
            Cell dataCell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (dataCell == null || dataCell.getCellType() == CellType.BLANK) {
                return false;
            }
        }

        return true;
    }

    private void validateNotEmpty(Sheet sheet) {
        if (sheet.getLastRowNum() < HEADER_ROW_INDEX + 2) {
            logger.error("Sheet validation failed: less than 3 rows present");
            throw new RuntimeException("Sheet must contain at least MPRN, header and one data row, which is 3 rows in total");
        }
    }

    private void validateHeaderRow(Row headerRow) {
        List<LocalTime> expected = expectedTimeSlots.getSlots();
        List<LocalTime> actual = new ArrayList<>();

        if (!headerRow.getCell(0).getStringCellValue().contains("Date")) {
            logger.error("Header row missing \"Date\" column at index 0");
            throw new RuntimeException("Missing date column");
        }

        for (int columnIndex = 1; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
            Cell cell = headerRow.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                logger.error("Missing time slot cell in column {}", columnIndex + 1);
                throw new RuntimeException("Missing time slot in column " + columnIndex);
            }
            actual.add(cell.getLocalDateTimeCellValue().toLocalTime());
        }

        if (!actual.equals(expected)) {
            logger.error("Time slots mismatch");
            throw new RuntimeException("Time slots mismatch");
        }
    }
}
