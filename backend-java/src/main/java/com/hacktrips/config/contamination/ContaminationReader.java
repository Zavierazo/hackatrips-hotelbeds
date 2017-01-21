package com.hacktrips.config.contamination;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContaminationReader {

    private static final Map<String, ContaminationData> CONTAMINATION;
    static {
        CONTAMINATION = new HashMap<>();
        CONTAMINATION.put("28079035", new ContaminationData(40.4185841, -3.7056038));
        CONTAMINATION.put("28079004", new ContaminationData(40.4241618, -3.7149907));
        CONTAMINATION.put("28079039", new ContaminationData(40.4771599, -3.7179277));
        CONTAMINATION.put("28079008", new ContaminationData(40.42164, -3.683806));
        CONTAMINATION.put("28079038", new ContaminationData(40.4518824, -3.7057832));
        CONTAMINATION.put("28079011", new ContaminationData(40.4524419, -3.6708858));
        CONTAMINATION.put("28079040", new ContaminationData(40.381391, -3.6259737));
        CONTAMINATION.put("28079016", new ContaminationData(40.4558329, -3.65837));
        CONTAMINATION.put("28079017", new ContaminationData(40.3416341, -3.7146667));
        CONTAMINATION.put("28079018", new ContaminationData(40.3950162, -3.7339255));
        CONTAMINATION.put("28079036", new ContaminationData(40.4056113, -3.660209));
        CONTAMINATION.put("28079024", new ContaminationData(40.4263596, -3.7609344));
        CONTAMINATION.put("28079027", new ContaminationData(40.4839402, -3.5701402));
        CONTAMINATION.put("28079047", new ContaminationData(40.3956041, -3.6804147));
        CONTAMINATION.put("28079048", new ContaminationData(40.4804464, -3.6869708));
        CONTAMINATION.put("28079049", new ContaminationData(40.4080653, -3.6936071));
        CONTAMINATION.put("28079050", new ContaminationData(40.4659812, -3.692042));
        CONTAMINATION.put("28079054", new ContaminationData(40.3598441, -3.5934287));
        CONTAMINATION.put("28079055", new ContaminationData(40.4612134, -3.5836995));
        CONTAMINATION.put("28079056", new ContaminationData(40.3850804, -3.7195119));
        CONTAMINATION.put("28079057", new ContaminationData(40.4936448, -3.659335));
        CONTAMINATION.put("28079058", new ContaminationData(40.519892, -3.78216));
        CONTAMINATION.put("28079059", new ContaminationData(40.4600819, -3.6170818));
        CONTAMINATION.put("28079060", new ContaminationData(40.4984271, -3.6940228));
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File csvData = new File("src/main/resources/contaminacion.csv");
        CSVParser parser = CSVParser.parse(IOUtils.toString(new FileInputStream(csvData)), CSVFormat.DEFAULT);
        for (CSVRecord csvRecord : parser) {
            String id = csvRecord.get(0) + csvRecord.get(1) + csvRecord.get(2);
            if (csvRecord.get(5).equals("02")) {
                if (CONTAMINATION.containsKey(id)) {
                    ContaminationData contaminationData = CONTAMINATION.get(id);
                    String anyo = csvRecord.get(6);
                    String mes = csvRecord.get(7);
                    String dia = csvRecord.get(8);

                    if (csvRecord.get(3).equals("06") || csvRecord.get(3).equals("42")
                            || csvRecord.get(3).equals("44")) {
                        log.info("Ignored " + id + " on " + anyo + "-" + mes + "-" + dia + "tiene " + csvRecord.get(9));
                    } else {
                        int hora = 1;
                        if (contaminationData.getContaminationByHour().containsKey(hora)) {

                        }
                        contaminationData.getContaminationByHour().put(hora, Double.valueOf(csvRecord.get(9)));

                        //                        log.info(id + " on " + anyo + "-" + mes + "-" + dia + "tiene " + csvRecord.get(9));
                    }
                } else {
                    log.error("La id {} no existe", id);
                }
            } else {
                log.info("Es diarios!!");
            }
        }
    }



}
