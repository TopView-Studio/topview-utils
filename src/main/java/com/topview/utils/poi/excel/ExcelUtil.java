package com.topview.utils.poi.excel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author MagicianZeng
 * @date 2019/6/28 16:16
 * @description
 */
public class ExcelUtil {
    /**
     * 少量数据读取
     *
     * @param inputStream 输入流
     * @param sheetNo     sheet的索引值(从0开始)
     * @param <T>         每行对应的实体类
     * @return 实体类集合
     */
    public static <T> List<T> simpleReadFromExcel(InputStream inputStream, int sheetNo,
                                                  Class<T> clazz) {
        return simpleReadFromExcel(inputStream, sheetNo, 1, clazz);
    }

    /**
     * 少量数据读取
     *
     * @param inputStream   输入流
     * @param sheetNo       sheet的索引值(从0开始)
     * @param headRowNumber 表头的行数(默认1)
     * @param <T>           每行对应的实体类
     * @return 实体类集合
     */
    public static <T> List<T> simpleReadFromExcel(InputStream inputStream, int sheetNo, int headRowNumber,
                                                  Class<T> clazz) {
        return EasyExcel
                .read(inputStream)
                .sheet(sheetNo)
                .headRowNumber(headRowNumber)
                .head(clazz)
                .doReadSync();
    }

    /**
     * 大量数据(1000行以上)的分批sax读取
     *
     * @param inputStream 输入流
     * @param sheetNo     sheet的索引值(从0开始)
     * @param <T>         每行对应的实体类
     * @param doSomething sax分批中每批的处理方法
     */
    public static <T> void saxReadFromExcel(InputStream inputStream, int sheetNo,
                                            Class<T> clazz, Consumer<List<T>> doSomething) {
        saxReadFromExcel(inputStream, sheetNo, 1, clazz, doSomething);
    }

    /**
     * 大量数据(1000行以上)的分批sax读取
     *
     * @param inputStream   输入流
     * @param sheetNo       sheet的索引值(从0开始)
     * @param headRowNumber 表头的行数(默认1)
     * @param <T>           每行对应的实体类
     * @param doSomething   sax分批中每批的处理方法
     */
    public static <T> void saxReadFromExcel(InputStream inputStream, int sheetNo, int headRowNumber,
                                            Class<T> clazz, Consumer<List<T>> doSomething) {
        AnalysisEventListener<T> listener = new AnalysisEventListener<T>() {
            private List<T> excelData = new ArrayList<>();

            @Override
            public void invoke(T t, AnalysisContext context) {
                excelData.add(t);
                if (excelData.size() >= 100) {
                    doSomething.accept(excelData);
                    excelData = new ArrayList<>();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                doSomething.accept(excelData);
            }
        };
        EasyExcel.read(inputStream, listener).sheet(sheetNo).headRowNumber(headRowNumber).head(clazz).doRead();
    }

    /**
     * 写入表格到流，默认自动关闭流
     *
     * @param sheetName    sheet名
     * @param outputStream 写入的流
     * @param modelClass   表头格式（优先使用）
     * @param headList     表头名列表
     * @param dataList     数据列表
     * @param sheetNo      sheet页数
     * @param headLineMun  起始行（由于新版easy excel更新了该项意义，故弃用）
     * @param <T>          数据类
     */
    public static <T> void writeToExcel(String sheetName, OutputStream outputStream,
                                        Class<T> modelClass, List<List<String>> headList,
                                        List<T> dataList, int sheetNo, int headLineMun) {
        writeToExcel(sheetName, outputStream, modelClass, headList, dataList, sheetNo, headLineMun, true);
    }

    /**
     * 写入表格到流
     *
     * @param sheetName    sheet名
     * @param outputStream 写入的流
     * @param modelClass   表头格式（优先使用）
     * @param headList     表头名列表
     * @param dataList     数据列表
     * @param sheetNo      sheet页数
     * @param headLineMun  起始行（由于新版easy excel更新了该项意义，故弃用）
     * @param autoClose    自动关闭流
     * @param <T>          数据类
     */
    public static <T> void writeToExcel(String sheetName, OutputStream outputStream,
                                        Class<T> modelClass, List<List<String>> headList,
                                        List<T> dataList, int sheetNo, int headLineMun, boolean autoClose) {
        // 创建表头格式
        WriteCellStyle headCellStyle = new WriteCellStyle();
        WriteFont headFont = new WriteFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBold(false);
        headFont.setColor(Font.COLOR_NORMAL);
        headCellStyle.setWriteFont(headFont);
        headCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 创建内容格式
        WriteFont contentFont = new WriteFont();
        contentFont.setFontName("宋体");
        WriteCellStyle contentCellStyle = new WriteCellStyle();
        contentFont.setColor(Font.COLOR_NORMAL);
        contentCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentCellStyle.setWriteFont(contentFont);

        // 初始化全表格式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headCellStyle, contentCellStyle);

        // 构造写入器
        ExcelWriterBuilder excelWriterBuilder = new ExcelWriterBuilder();
        excelWriterBuilder
                .file(outputStream)
                .excelType(ExcelTypeEnum.XLSX)
                .needHead(true)
                .autoCloseStream(autoClose)
                //表格式
                .registerWriteHandler(horizontalCellStyleStrategy)
                //自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                //行高设置
                .registerWriteHandler(new RowWriteHandler() {
                    @Override
                    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex, Integer relativeRowIndex, Boolean isHead) {

                    }

                    @Override
                    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {

                    }

                    @Override
                    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
                        if (isHead) {
                            row.setHeightInPoints(21.50f);
                        } else {
                            row.setHeightInPoints(18.00f);
                        }
                    }
                });
        ExcelWriter writer = excelWriterBuilder.build();

        // 构造表格属性
        WriteSheet sheet = new WriteSheet();
        sheet.setSheetNo(sheetNo);
        // 首选class作为表头类型
        if (modelClass != null) {
            sheet.setClazz(modelClass);
        } else {
            sheet.setHead(headList);
        }
        sheet.setSheetName(sheetName);
        //sheet.setRelativeHeadRowIndex(headLineMun);
        writer.write(dataList, sheet);

        writer.finish();
    }

    private static void setBorderCell(CellStyle defaultCellStyle) {
        defaultCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        defaultCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        defaultCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        defaultCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        defaultCellStyle.setBorderLeft(BorderStyle.THIN);
        defaultCellStyle.setBorderRight(BorderStyle.THIN);
        defaultCellStyle.setBorderTop(BorderStyle.THIN);
        defaultCellStyle.setBorderBottom(BorderStyle.THIN);
    }
}
