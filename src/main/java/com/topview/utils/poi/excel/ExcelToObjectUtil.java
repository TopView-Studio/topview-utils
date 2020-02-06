package com.topview.utils.poi.excel;

import com.topview.utils.empty.IsEmptyUtil;

import java.util.List;
import java.util.Map;

/**
 * @author yongPhone
 * @date on 2018/4/18
 */
public class ExcelToObjectUtil {

    private static final String DEFAULT_SHEET_NAME = "sheet1";

    private static final String DEFAULT_FILE_PATH_PREFIX = "/excel/";

    private static final String XLS_SUFFIX = ".xls";

    private static final String XNLS_SUFFIX = ".xnls";

    public static <T> void exportExcel(String sheetName, String filePath, Map<String, String> headMap,
                                       List<T> dataList, Class<T> clazz) {
        sheetName = getAvailableSheetName(sheetName);
        filePath = getAvailableFilePath(filePath);
    }

    private static String getAvailableSheetName(String sheetName) {
        return IsEmptyUtil.isEmpty(sheetName) ? DEFAULT_SHEET_NAME : sheetName;
    }

    private static String getAvailableFilePath(String filePath) {
        return IsEmptyUtil.isEmpty(filePath) ? getDefaultFilePath() : filePath;
    }

    private static String getDefaultFilePath() {
        return DEFAULT_FILE_PATH_PREFIX + System.currentTimeMillis() + XLS_SUFFIX;
    }

    public static String getDefaultSheetName() {
        return DEFAULT_SHEET_NAME;
    }
}