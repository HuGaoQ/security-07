package com.ncamc.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ncamc.entity.Product;
import com.ncamc.entity.ResponseResult;
import com.ncamc.service.ProductService;
import com.ncamc.utils.RedisCache;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TextSelection;
import com.spire.doc.documents.TextWrappingStyle;
import com.spire.doc.fields.DocPicture;
import com.wisdge.web.springframework.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-08 09:44
 */
@Slf4j
@Api("产品product接口")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisCache redisCache;

    public static final String CACHE_KEY_USER = "user:";

    @ApiOperation("多表分页模糊条件查询")
    @PostMapping("/lists")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataTypeClass = Integer.class, example = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataTypeClass = Integer.class, example = "当前页条数"),
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "", dataTypeClass = String.class, example = "用户名"),
            @ApiImplicitParam(name = "id", value = "ID", defaultValue = "", dataTypeClass = Integer.class, example = "ID")
    })
    public ResponseResult getProductList(@RequestBody Map<String, Object> params){
        return productService.getProductList(params);
    }

    @ApiOperation("查询分页信息")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataTypeClass = Integer.class, example = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataTypeClass = Integer.class, example = "当前页条数"),
            @ApiImplicitParam(name = "prdIns", value = "产品名称", defaultValue = "", dataTypeClass = String.class, example = "机构名称")
    })
    public ResponseResult list(@RequestBody Map<String, Object> params) {
        return productService.listPage(params);
    }

    @ApiOperation("数据库新增记录")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Product product) {
        try {
            SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Product> products =  productService.list();
            product.setId(products.size()+1);
            product.setNewDate(newDate.format(new Date()));
            product.setNewTime(newTime.format(new Date()));
            boolean b = productService.save(product);
            if (b) {
                product = productService.selectByPrimaryKey(product.getId());
                redisCache.setCacheObject(CACHE_KEY_USER + product.getId(), product);
            }
            return new ResponseResult(HttpStatus.OK.value(), "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "添加失败");
        }
    }

    @ApiOperation("查询1条记录")
    @GetMapping("/findById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataTypeClass = Integer.class, example = "ID")
    })
    public ResponseResult select(Long id) {
        return new ResponseResult(HttpStatus.OK.value(), productService.findById(id));
    }

    @ApiOperation("修改产品信息")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody Product product) {
        try {
            UpdateWrapper wrapper = new UpdateWrapper<>();
            wrapper.eq("id", product.getId());
            boolean b = productService.update(product, wrapper);
            if (b) {
                redisCache.deleteObject(CACHE_KEY_USER + product.getId());
                redisCache.setCacheObject(CACHE_KEY_USER + product.getId(), product);
            }
            return new ResponseResult(HttpStatus.OK.value(), "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改失败");
        }
    }

    @ApiOperation("删除1条记录")
    @GetMapping("/del")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataTypeClass = Integer.class, example = "ID")
    })
    public ResponseResult del(Long id) {
        return new ResponseResult(HttpStatus.OK.value(), "删除成功", productService.deleteByPrimaryKey(id));
    }

    @ApiOperation("修改对应WPS文件")
    @GetMapping("/wps")
    public String wps() throws IOException {
        //通过流加载WPS文字文档
        FileInputStream inputStream = new FileInputStream("F:/L/东京奥运会.wps");
        Document document = new Document();
        document.loadFromStream(inputStream, FileFormat.Doc);

        //查找所有“北京冬奥会”文本
        TextSelection[] textSelections = document.findAllString("国", false, false);
        //设置文本高亮色、加粗
        for (TextSelection selection : textSelections) {
            selection.getAsOneRange().getCharacterFormat().setHighlightColor(Color.YELLOW);
            selection.getAsOneRange().getCharacterFormat().setBold(true);
        }

        //获取文档的第一个节
        Section section = document.getSections().get(0);

        //获取第2段，设置段落背景色
        Paragraph paragraph1 = section.getParagraphs().get(1);
        paragraph1.getFormat().setBackColor(new Color(176, 224, 230));
        paragraph1.getStyle().getParagraphFormat().setHorizontalAlignment(com.spire.doc.documents.HorizontalAlignment.Center);

        //获取第3段，添加图片到段落
        Paragraph paragraph2 = section.getParagraphs().get(2);
        DocPicture picture = paragraph2.appendPicture("F:/PNG/shouye1.png");
        picture.setWidth(200f);
        picture.setHeight(250f);
        picture.setTextWrappingStyle(TextWrappingStyle.Through);


        //将结果文档保存到流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        document.saveToStream(bos, FileFormat.Doc);
        //将流写入WPS文档
        FileOutputStream fos = new FileOutputStream("Output.wps");
        fos.write(bos.toByteArray());
        //关闭流
        bos.close();
        fos.close();
        return "success";
    }

    @ApiOperation("导出产品信息")
    @GetMapping("/exprot")
    public void exprot(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = String.format("Product_%s.xlsx", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        List<Product> list = productService.list();
        System.out.println(list);
        if (list == null) {
            return;
        }

        byte[] reviewBytes = generatorProductProspectRecordFile(list);
        WebUtils.out(request, response, reviewBytes, fileName, Boolean.TRUE);
    }

    public static byte[] generatorProductProspectRecordFile(List<Product> resultList) throws Exception {
        //创建工作薄
        XSSFWorkbook wk = new XSSFWorkbook();
        //创建表单
        XSSFSheet sheet = genSheet(wk,"产品信息Test");
        //创建表单样式
        XSSFCellStyle titleStyle = genTitleStyle(wk);//创建标题样式
        XSSFCellStyle contextStyle = genContextStyle(wk);//创建文本样式

        //根据Excel列名长度，指定列名宽度  Excel总共10列
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                sheet.setColumnWidth(i, 1500);
            } else if (i == 2 || i == 3 || i == 4) {
                sheet.setColumnWidth(i, 4000);
            } else if (i == 9) {
                sheet.setColumnWidth(i, 3000);
            } else {
                sheet.setColumnWidth(i, 2000);
            }
        }

        //创建第一行，为标题，index从0开始
        XSSFRow row0 = sheet.createRow(0);
        XSSFCell cell = row0.createCell(0);//创建一列
        cell.setCellValue("产品信息");//标题
        cell.setCellStyle(titleStyle);//设置标题样式

        //合并第一行单元格
        CellRangeAddress titleRegion = new CellRangeAddress(0, 0, 0, 9);
        sheet.addMergedRegion(titleRegion);

        //创建第二行
        XSSFRow row = sheet.createRow(1);
        XSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("Id");
        cell0.setCellStyle(contextStyle);
        XSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("产品名称");
        cell1.setCellStyle(contextStyle);
        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("产品代码");
        cell2.setCellStyle(contextStyle);
        XSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("净值");
        cell3.setCellStyle(contextStyle);
        XSSFCell cell4 = row.createCell(4);
        cell4.setCellValue("份额");
        cell4.setCellStyle(contextStyle);
        XSSFCell cell5 = row.createCell(5);
        cell5.setCellValue("金额");
        cell5.setCellStyle(contextStyle);
        XSSFCell cell6 = row.createCell(6);
        cell6.setCellValue("机构名称");
        cell6.setCellStyle(contextStyle);
        XSSFCell cell7 = row.createCell(7);
        cell7.setCellValue("机构代码");
        cell7.setCellStyle(contextStyle);
        XSSFCell cell8 = row.createCell(8);
        cell8.setCellValue("日期");
        cell8.setCellStyle(contextStyle);
        XSSFCell cell9 = row.createCell(9);
        cell9.setCellValue("当前时间");
        cell9.setCellStyle(contextStyle);

        //填入数据
        for (int i = 0; i < resultList.size(); i++) {
            Product product = resultList.get(i);
            XSSFRow curRow = sheet.createRow(i+2);
            // ID
            XSSFCell curCell = curRow.createCell(0);
            curCell.setCellValue(product.getId());
            curCell.setCellStyle(contextStyle);
            //产品名称
            XSSFCell curCell1 = curRow.createCell(1);
            curCell1.setCellValue(product.getPrdName());
            curCell1.setCellStyle(contextStyle);
            // 产品代码
            XSSFCell curCell2 = curRow.createCell(2);
            curCell2.setCellValue(product.getPrdDm());
            curCell2.setCellStyle(contextStyle);
            // 净值
            XSSFCell curCell3 = curRow.createCell(3);
            curCell3.setCellValue(product.getNet());
            curCell3.setCellStyle(contextStyle);
            // 份额
            XSSFCell curCell4 = curRow.createCell(4);
            curCell4.setCellValue(product.getFbalance());
            curCell4.setCellStyle(contextStyle);
            // 金额
            XSSFCell curCell5 = curRow.createCell(5);
            curCell5.setCellValue(product.getFavalable());
            curCell5.setCellStyle(contextStyle);
            // 机构名称
            XSSFCell curCell6 = curRow.createCell(6);
            curCell6.setCellValue(product.getInsName());
            curCell6.setCellStyle(contextStyle);
            //机构代码
            XSSFCell curCell7 = curRow.createCell(7);
            curCell7.setCellValue(product.getInsDm());
            curCell7.setCellStyle(contextStyle);
            //日期
            XSSFCell curCell8 = curRow.createCell(8);
            curCell8.setCellValue(product.getNewDate());
            curCell8.setCellStyle(contextStyle);
            //当前时间
            XSSFCell curCell9 = curRow.createCell(9);
            curCell9.setCellValue(product.getNewTime());
            curCell9.setCellStyle(contextStyle);
        }

        // 更新excel公式，自动计算
        wk.getCreationHelper().createFormulaEvaluator().evaluateAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wk.write(outputStream);
        wk.close();
        return outputStream.toByteArray();
    }

    //设置表单，并生成表单
    public static XSSFSheet genSheet(XSSFWorkbook workbook, String sheetName){
        //生成表单
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //设置表单文本居中
        sheet.setHorizontallyCenter(true);
        sheet.setFitToPage(false);
        //打印时在底部右边显示文本页信息
        Footer footer = sheet.getFooter();
        footer.setRight( "Page " + HeaderFooter.numPages()+ " Of "+ HeaderFooter.page());
        //打印时在头部右边显示Excel创建日期信息
        Header header = sheet.getHeader();
        header.setRight("Create Date " + HeaderFooter.date() + " " + HeaderFooter.time());
        //设置打印方式
        XSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setLandscape(true); // true：横向打印，false：竖向打印 ，因为列数较多，推荐在打印时横向打印
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //打印尺寸大小设置为A4纸大小
        return sheet;
    }

    //生成标题样式
    public static XSSFCellStyle genTitleStyle(XSSFWorkbook workbook) {

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        //标题居中，没有边框，所以这里没有设置边框，设置标题文字样式
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);//加粗
        titleFont.setFontHeight((short) 15);//文字尺寸
        titleFont.setFontHeightInPoints((short) 15);
        style.setFont(titleFont);

        return style;
    }

    //创建文本样式
    public static XSSFCellStyle genContextStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//文本水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//文本竖直居中显示
        style.setWrapText(true);//文本自动换行

        //生成Excel表单，需要给文本添加边框样式和颜色
        /*
             CellStyle.BORDER_DOUBLE      双边线
             CellStyle.BORDER_THIN        细边线
             CellStyle.BORDER_MEDIUM      中等边线
             CellStyle.BORDER_DASHED      虚线边线
             CellStyle.BORDER_HAIR        小圆点虚线边线
             CellStyle.BORDER_THICK       粗边线
         */
        style.setBorderBottom(BorderStyle.THIN);//设置文本边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //set border color
        style.setTopBorderColor(new XSSFColor(Color.BLACK));//设置文本边框颜色
        style.setBottomBorderColor(new XSSFColor(Color.BLACK));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK));
        style.setRightBorderColor(new XSSFColor(Color.BLACK));

        return style;
    }

}