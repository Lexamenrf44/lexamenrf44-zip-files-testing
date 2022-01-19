package com.lexamenrf44;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileTest {

    @Test
    void ZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/ZipTestArchive.zip");

        ZipEntry xlsEntry = zipFile.getEntry("mytesttableone.xlsx");
        try (InputStream stream = zipFile.getInputStream(xlsEntry)) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue())
                    .isEqualTo("75555555555");
        }

        ZipEntry csvEntry = zipFile.getEntry("mytesttabletwo.csv");
        try (InputStream stream = zipFile.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();
            assertThat(list)
                    .hasSize(5)
                    .contains(
                            new String[]{"NAME", "GENDER"},
                            new String[]{"Ilya", "M"},
                            new String[]{"Olga", "F"},
                            new String[]{"Sergey", "M"},
                            new String[]{"Grigory", "M"}
                    );
        }

        ZipEntry pdfEntry = zipFile.getEntry("generic.pdf");
        try (InputStream stream = zipFile.getInputStream(pdfEntry)) {
            PDF parsed = new PDF(stream);
            assertThat(parsed.text).contains("богатый опыт рамки");
        }
    }
}
