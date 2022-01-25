package com.csicit.ace.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.aspose.cells.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 18:20
 */
public class ExcelUtil {
    /**
     * 下载excel
     *
     * @param request  http请求
     * @param response http响应
     * @param headers  标题的键值对 键是标题的标识  值是标题内容
     * @param data     数据的键值对  键是标题的标识  值是数据内容
     * @param fileName 文件名
     * @author FourLeaves
     * @date 2020/4/13 8:52
     */
    public static void OutFileToStream(HttpServletRequest request, HttpServletResponse response, Map<String, String> headers, List<? extends Object> data, String fileName){
        Workbook workbook = new Workbook();
        WorksheetCollection worksheets = workbook.getWorksheets();
        Worksheet worksheet = worksheets.get(0);
        Cells cells = worksheet.getCells();

        //设置标题样式
        Style HeaderStyle = workbook.createStyle();
        HeaderStyle.getFont().setBold(true);  //文字加粗
        HeaderStyle.setName("宋体");  //文字字体
        HeaderStyle.getFont().setSize(13);  //文字大小
        HeaderStyle.setHorizontalAlignment(TextAlignmentType.CENTER);
        HeaderStyle.setTextWrapped(true);//单元格内容自动换行

        //设置内容样式
        Style cellsStyle = workbook.createStyle();
        cellsStyle.setHorizontalAlignment(TextAlignmentType.CENTER);  //居中
        HeaderStyle.setName("宋体");  //文字字体
        HeaderStyle.getFont().setSize(12);  //文字大小
        cellsStyle.setTextWrapped(true);//单元格内容自动换行

        Map<String, Integer> keyIndex = new HashMap<>();

        int count = 0;
        for (Map.Entry<String, String> header : headers.entrySet()) {
            keyIndex.put(header.getKey(), count);
            cells.get(0, count).setValue(header.getValue());
            cells.get(0, count).setStyle(HeaderStyle);
            cells.setRowHeight(0, 25);
            cells.setColumnWidth(count, 25);
            count++;
        }

        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = JsonUtils.castObject(data.get(i), JSONObject.class);
            for (String key : jsonObject.keySet()) {
                if (keyIndex.containsKey(key)) {
                    int j = keyIndex.get(key);
                    cells.get(i + 1, j).setValue(jsonObject.get(key));
                    cells.get(i + 1, j).setStyle(cellsStyle);
                    cells.setRowHeight(j, 20);
                    cells.setColumnWidth(j, 38);
                }
            }
        }
        if (StringUtils.isEmpty(fileName)) {
            fileName = "AceExportData.xlsx";
        }
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "filename=" + fileName);
            response.addHeader("filename", fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            workbook.save(response.getOutputStream(), SaveFormat.XLSX);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 下载excel
     *
     * @param request  http请求
     * @param response http响应
     * @param headers  标题的键值对 键是标题的标识  值是标题内容
     * @param data     数据的键值对  键是标题的标识  值是数据内容
     * @author FourLeaves
     * @date 2020/4/13 8:52
     */
    public static void OutFileToStream(HttpServletRequest request, HttpServletResponse response, Map<String, String> headers, List<? extends Object> data){
        OutFileToStream(request, response, headers, data, null);
    }

    /**
     * 下载excel
     *
     * @param response http响应
     * @param headers  标题的键值对 键是标题的标识  值是标题内容
     * @param data     数据的键值对  键是标题的标识  值是数据内容
     * @author FourLeaves
     * @date 2020/4/13 8:52
     */
    public static void OutFileToStream( HttpServletResponse response, Map<String, String> headers, List<? extends Object> data){
        OutFileToStream(response, headers, data, null);
    }

    /**
     * 下载excel
     *
     * @param response http响应
     * @param headers  标题的键值对 键是标题的标识  值是标题内容
     * @param data     数据的键值对  键是标题的标识  值是数据内容
     * @param fileName 文件名
     * @author FourLeaves
     * @date 2020/4/13 8:52
     */
    public static void OutFileToStream( HttpServletResponse response, Map<String, String> headers, List<? extends Object> data, String fileName){
        OutFileToStream(null, response, headers, data, fileName);
    }
}
