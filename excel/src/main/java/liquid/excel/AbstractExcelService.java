package liquid.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.stereotype.Service;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by redbrick9 on 6/14/14.
 */
@Service
public class AbstractExcelService<E> {
    public List<E> extract(InputStream inputStream, final Class<E> clazz, final RowMapper<E> rowMapper) throws IOException {
        final List<E> list = new ArrayList<>();

        OPCPackage opcPackage = null;
        XSSFReader reader = null;
        final SharedStringsTable sharedStringsTable;
        XMLReader parser = null;
        Iterator<InputStream> sheets = null;
        try {
            opcPackage = OPCPackage.open(inputStream);
            reader = new XSSFReader(opcPackage);
            sharedStringsTable = reader.getSharedStringsTable();
            reader.getWorkbookData();
            sheets = reader.getSheetsData();
        } catch (OpenXML4JException e) {
            throw new ExcelOperationException(e);
        }

        ContentHandler handler = new DefaultHandler() {
            private String lastContents;
            private boolean nextIsString;
            private String r = "";
            private String s = "";
            boolean isDate = false;
            E entity;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("row")) {
                    //
                    try {
                        entity = clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                // c => cell
                if (qName.equals("c")) {
                    // Print the cell reference
                    r = attributes.getValue("r");
                    s = attributes.getValue("s");

                    // Figure out if the value is an index in the SST
                    String cellType = attributes.getValue("t");

                    if (cellType != null && cellType.equals("s")) {
                        nextIsString = true;
                    } else {
                        nextIsString = false;
                    }
                    if (null == cellType)
                        isDate = DateUtil.isInternalDateFormat(Integer.valueOf(s));
                }
                // Clear contents cache
                lastContents = "";
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                // Process the last contents as required.
                // Do now, as characters() may be called more than once
                if (nextIsString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx)).toString();
                    nextIsString = false;
                }

                // v => contents of a cell
                // Output after we've seen the string contents
                if (qName.equals("v")) {
                    if (isDate) {
                        Date date = DateUtil.getJavaDate(Double.valueOf(lastContents));
                        rowMapper.translate(entity, r, date);
                    } else {
                        rowMapper.translate(entity, r, lastContents);
                    }
                }
                if (qName.equals("c")) {
                    r = "";
                    s = "";
                    isDate = false;
                }
                if (qName.equals("row")) {
                    if (rowMapper.validate(entity)) list.add(entity);
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                lastContents += new String(ch, start, length);
            }
        };

        try {
            parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            parser.setContentHandler(handler);

            while (sheets.hasNext()) {
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
        } catch (SAXException e) {
            throw new ExcelOperationException(e);
        }
        return list;
    }
}
