package io.github.tekisho.elconsumptionaggregator.util;

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public final class StyleProvider {
    private final Workbook workbook;
    private final Map<StyleKey, CellStyle> styles = new HashMap<>();

    public enum StyleKey{
        MPRN,
        HEADER,
        DATA
    }

    public StyleProvider(Workbook workbook) {
        this.workbook = workbook;

        initStyles();
    }

    private void initStyles() {
        var mprnFont = workbook.createFont();
        mprnFont.setFontHeightInPoints((short) 10);
        mprnFont.setFontName("Arial");

        var mprnCellStyle = workbook.createCellStyle();
        mprnCellStyle.setFont(mprnFont);
        mprnCellStyle.setAlignment(HorizontalAlignment.CENTER);
        styles.put(StyleKey.MPRN, mprnCellStyle);

        var defaultFont = workbook.createFont();
        defaultFont.setFontHeightInPoints((short) 11);
        defaultFont.setFontName("Aptos Narrow");

        var headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(defaultFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put(StyleKey.HEADER, headerCellStyle);

        var dataCellStyle = workbook.createCellStyle();
        dataCellStyle.setFont(defaultFont);
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        styles.put(StyleKey.DATA, dataCellStyle);
    }

    public CellStyle getStyle(StyleKey key) {
        return styles.get(key);
    }
}
