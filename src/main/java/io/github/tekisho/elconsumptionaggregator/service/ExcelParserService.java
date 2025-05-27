package io.github.tekisho.elconsumptionaggregator.service;

import io.github.tekisho.elconsumptionaggregator.model.ConsumptionDay;
import io.github.tekisho.elconsumptionaggregator.model.TimeSlots;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ExcelParserService {
    private final TimeSlots timeSlots;

    public ExcelParserService(TimeSlots timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<ConsumptionDay> parse(File file) {
        List<ConsumptionDay> days = new ArrayList<>();

        Workbook workbook = null;
        try {
            // accessing file
            workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            // validation
            validateFormat(headerRow);

            // parsing
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                String dayId = row.getCell(0).getStringCellValue();

                List<Double> values = new ArrayList<>();
                for (int colIndex = 1; colIndex < row.getLastCellNum(); colIndex++) {
                    values.add(row.getCell(colIndex).getNumericCellValue());
                }

                days.add(new ConsumptionDay(dayId, values));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse excel file", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch(IOException ioe) {
                    // logging ?
                }
            }
        }

        return days;
    }

    // TODO implement proper file format validation
    private void validateFormat(Row headerRow) {
        List<LocalTime> expectedSlots = timeSlots.getSlots();
        for (int colIndex = 1; colIndex < headerRow.getLastCellNum(); colIndex++) {

        }


        // 1 - time intervals
//        List<LocalTime> expectedSlots = timeSlots.getSlots();
//        for (int colIndex = 1; colIndex < headerRow.getLastCellNum(); colIndex++) {
//            String timeStr = headerRow.getCell(colIndex).getStringCellValue();
//            LocalTime time = LocalTime.parse(timeStr);
//
//            if (!expectedSlots.contains(time)) {
//                throw new RuntimeException("Wrong time interval: " + timeStr);
//            }
//        }

        // 2 - all consumption rows should be in expected format
//        for (int rowIndex = 1; rowIndex < headerRow.getLastCellNum(); rowIndex++) {
//            for (int colIndex = 0; colIndex < headerRow.getLastCellNum(); colIndex++) {
//                // TODO
//            }
//        }
    }
}
