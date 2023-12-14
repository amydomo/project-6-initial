package com.example.dictionary.reference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;


/*add annotation: when dictionary application code starts, spring will go out into our code base
and will say give me all the classes that have a component annotation on top of them because I need to do something with them.
 */
@Component
public class DictionaryReference {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryReference.class); //passing DictionaryReference class

    //field to store all of the dictionary
    private static Map<String, String> dictionary;

    //static block

    static {

        try {
            readDictionaryFile();       //instantiates dictionary Map
        } catch (JsonProcessingException e) {
            logger.error("Failed to read dictionary file");     //logs there is a problem reading dictionary file
        }
    }

    //We don't want to instantiate this class, so we block it so no one accidentally instantiates it.
    private DictionaryReference(){      //block instantiation

    }

    private static void readDictionaryFile() throws JsonProcessingException {

        //generate symmetrics for our logs

        StopWatch sw = new StopWatch();       //start stopwatch (object from Spring)
        sw.start();

        InputStream inputStream = DictionaryReference.class.getClassLoader()
                .getResourceAsStream("dictionary.json");

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);   //taking input stream and wrapping it
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);      //buffer reader
        String json = bufferedReader.lines()
                .collect(Collectors.joining("\n"));         //turns dictionary into a single string

        ObjectMapper mapper = new ObjectMapper();
        dictionary = mapper.readValue(json, Map.class);         //populates dictionary

        sw.stop();  //stop stopwatch

        long milliseconds = sw.getLastTaskTimeMillis();         //pc system keeps track of this

        String message = new StringBuilder().append("Dictionary created with ")     //create string outside of log line...
                .append(dictionary.size())
                .append(" entries in ")
                .append(milliseconds)
                .append("ms")
                .toString();

        logger.info(message);      //...& then log it

    }

    public static Map<String, String> getDictionary(){
        return DictionaryReference.dictionary;

    }
}
