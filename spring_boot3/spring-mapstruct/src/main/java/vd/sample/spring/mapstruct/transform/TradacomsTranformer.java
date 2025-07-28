package vd.sample.spring.mapstruct.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TradacomsTranformer {

    private static final Logger LOG = LoggerFactory.getLogger(TradacomsTranformer.class);

    protected Map<String, Object> ediReaderConfig;
//    protected EDIInputFactory ediInputFactory;
//    protected EDIStreamReader ediReader;


    public TradacomsTranformer()  {
        ediReaderConfig = new HashMap<>();
//        ediInputFactory = EDIInputFactory.newFactory();
    }


    protected void setupReader(InputStream ediStream, String schemaResource) throws Exception {
//        ediReaderConfig.forEach(ediInputFactory::setProperty);
//        ediReader = ediInputFactory.createEDIStreamReader(ediStream);
//
//        if (schemaResource != null) {
//            SchemaFactory schemaFactory = SchemaFactory.newFactory();
//            Resource resource = new ClassPathResource(schemaResource);
//            FileInputStream file = new FileInputStream(resource.getFile());
//            Schema transactionSchema = schemaFactory.createSchema(file);
////            URL brm = getClass().getResource(schemaResource);
////            Schema transactionSchema = schemaFactory.createSchema(getClass().getResource(schemaResource));
//            ediReader = ediInputFactory.createFilteredReader(ediReader, (reader) -> {
//                if (reader.getEventType() == EDIStreamEvent.START_TRANSACTION) {
//                    reader.setTransactionSchema(transactionSchema);
//                }
//                return true;
//            });
//        }
    }


    public void writeJsonToLog(InputStream stream) throws Exception {
//        ediReaderConfig.put(EDIInputFactory.JSON_NULL_EMPTY_ELEMENTS, true);
//        setupReader(stream, "schema/837.xml");
//        JsonParser jsonParser = JsonParserFactory.createJsonParser(ediReader, JsonParser.class, ediReaderConfig);
//        ObjectMapper objectMapper = new ObjectMapper();
//        jsonParser.setCodec(objectMapper);
//        JsonNode tree = jsonParser.readValueAsTree();
//        String prettyPrintedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
//        LOG.info("Readed EDI file : " + prettyPrintedJson);

    }

}
