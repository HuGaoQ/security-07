package com.ncamc;

import com.spire.doc.*;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TextSelection;
import com.spire.doc.documents.TextWrappingStyle;
import com.spire.doc.fields.DocPicture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.io.*;

@Slf4j
@SpringBootTest
public class TestDoc {

    @Test
    public void test1() throws IOException {
        //通过流加载WPS文字文档
        FileInputStream inputStream = new FileInputStream("F:/L/东京奥运会.wps");
        Document document = new Document();
        document.loadFromStream(inputStream, FileFormat.Doc);

        //查找所有“北京冬奥会”文本
        TextSelection[] textSelections = document.findAllString("国", false, false);
        //设置文本高亮色、加粗
        for (TextSelection selection : textSelections)
        {
            selection.getAsOneRange().getCharacterFormat().setHighlightColor(Color.YELLOW);
            selection.getAsOneRange().getCharacterFormat().setBold(true);
        }

        //获取文档的第一个节
        Section section = document.getSections().get(0);

        //获取第2段，设置段落背景色
        Paragraph paragraph1 = section.getParagraphs().get(1);
        paragraph1.getFormat().setBackColor(new Color(176,224,230));
        paragraph1.getStyle().getParagraphFormat().setHorizontalAlignment(HorizontalAlignment.Center);

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
    }
}