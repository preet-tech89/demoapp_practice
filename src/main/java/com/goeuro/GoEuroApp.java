/**
 * Copyright (C) 2016 Gurpreet Singh (preet.tech89@gmail.com)
 *
 * Modification Log
 * Date          Author                                         Remarks
 * Apr 22,2016   Gurpreet Singh (preet.tech89@gmail.com)        Created
 *
 */

package com.goeuro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Downloads the GoEuro API http://api.goeuro.com/api/v2/position/suggest/en/CITY_NAME data in CSV
 * file.
 * 
 * @author Gurpreet Singh (preet.tech89@gmail.com)
 */
public class GoEuroApp {

  /**
   * @param args holds the argument list.
   */
  public static void main(final String[] args) {

    // Check if the argument is being passed or not
    if (CommonUtils.isEmpty(args)) {
      System.err.println("Bad Request!! Pass the parameter");
    } else {
      // Fetch the parameter value.
      final String cityName = args[0];
      // Created to store apiUrl.
      final StringBuilder apiUrl = new StringBuilder();
      // Appending the city name in API end point URL to get final URL.
      apiUrl.append(Constants.API_END_POINT).append(cityName);
      try {

        // Gets the final API output in a string.
        final String finalApiOutput = getAPIJsonString(apiUrl.toString());

        if (finalApiOutput.matches(Constants.NUMBER_REGEX)) {
          System.err.println("Failed HTTP Connection- HTTP Error Code " + finalApiOutput);
          return;
        }

        // Created the list of location mapper for mapping the JSON data with object.
        final List<LocationMapper> locationMapperLst = new ArrayList<LocationMapper>();

        // Created the JSONArray from API output string.
        final JSONArray jsonArray = new JSONArray(finalApiOutput);

        // Iterating over the JSON array.
        for (int loopIdx = 0; loopIdx < jsonArray.length(); loopIdx++) {
          final JSONObject jsonObject = jsonArray.getJSONObject(loopIdx);

          final JSONObject geoLocatn = jsonObject.getJSONObject(Constants.GEO_POSITION);
          // Created location mapper instance to be added in a list.
          final LocationMapper locationMapper = new LocationMapper();

          locationMapper.set_id(jsonObject.getInt(Constants.LOCATION_ID));
          locationMapper.setName(jsonObject.getString(Constants.LOCATION_NAME));
          locationMapper.setType(jsonObject.getString(Constants.LOCATION_TYPE));
          locationMapper.setLatitude(geoLocatn.getDouble(Constants.LOCATION_LATITUDE));
          locationMapper.setLongitude(geoLocatn.getDouble(Constants.LOCATION_LONGITUDE));

          locationMapperLst.add(locationMapper);
        }

        if (CommonUtils.isEmpty(locationMapperLst)) {
          System.out.println("Empty result from API");
          return;
        }

        final String csvFileName = cityName + Constants.CSV_FILE_EXTNSN;
        // Writes the CSV File
        final String csvPath = writeCSVFile(csvFileName, locationMapperLst);

        if (!CommonUtils.isEmpty(csvPath)) {
          System.out.println("File has been created successfully at " + csvPath);
        }

      } catch (final MalformedURLException e) {
        System.err.println("Malformed API URL");

      } catch (final Exception exception) {
        System.err.println("An unexpected error has occured.");
      }
    }
  }

  /**
   * Gets the buffer reader data from API url using HTTP connection.
   * 
   * @param apiUrl holds the api end point url
   * @return BufferedReader holds the buffer reader data.
   * @throws IOException if any.
   */
  private static String getAPIJsonString(final String apiUrl) throws IOException {
    // Created the java.net.URL instance.
    final URL url = new URL(apiUrl);
    // Created the HttpURLConnection for opening connection to a url.
    final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
    // Sets the HTTP request method and property.
    httpConnection.setRequestMethod(Constants.HTTP_GET_REQUEST);
    httpConnection.setRequestProperty(Constants.HTTP_ACCEPT_KEY, Constants.HTTP_ACCEPT_TYPE);

    // If HTTP response connection is not 200 OK then return null;
    if (httpConnection.getResponseCode() != 200) {
      return String.valueOf(httpConnection.getResponseCode());
    }

    // Gets the buffer reader data
    final BufferedReader bufferReader =
      new BufferedReader(new InputStreamReader((httpConnection.getInputStream()),
        Constants.UTF_EIGHT_ENCODING));

    final StringBuilder bufferReaderOutput = new StringBuilder();
    String bufferReaderLine = Constants.EMPTY;

    while ((bufferReaderLine = bufferReader.readLine()) != null) {
      bufferReaderOutput.append(bufferReaderLine);
    }

    httpConnection.disconnect();

    return bufferReaderOutput.toString();
  }

  /**
   * Writes the location mapper data in CSV file using super csv library.
   * 
   * @param csvFileName holds the CSV file name.
   * @param locationMapperLst holds the list of location mapper objects.
   * @return String the full path of file being created.
   * @throws IOException if any.
   */
  private static String writeCSVFile(final String csvFileName,
      final List<LocationMapper> locationMapperLst) throws IOException {
    // Created the iCsv bean writer for writing data into csv file.
    ICsvBeanWriter beanWriter = null;
    final CellProcessor[] processors = new CellProcessor[] { new NotNull(), // id
      new Optional(), // name
      new Optional(), // type
      new Optional(new ParseDouble()), // latitude
      new Optional(new ParseDouble()) // longitude
      };
    // Gets the user profile from system property user.home
    final String userProfile =
      System.getProperty(Constants.SYSTEM_USER_HOME).replaceAll(Constants.FORWARD_SLASH,
        Constants.DOUBLE_SLASH);

    final StringBuilder csvFullPath = new StringBuilder();

    final String finalCsvDirectory =
      csvFullPath.append(userProfile).append(Constants.CSV_OUTPUT_DIRECTORY).toString();

    try {

      // Gets the complete path and create the directory.
      final Path csvFilePath = Paths.get(finalCsvDirectory);

      // if directory doesn't exist,create it.
      if (!Files.exists(csvFilePath)) {
        Files.createDirectories(csvFilePath);
      }

      // Gets the Writer instance with UTF-8 encoding.
      final Writer out =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalCsvDirectory
          + csvFileName), Constants.UTF_EIGHT_ENCODING));
      // Get the bean writer.
      beanWriter = new CsvBeanWriter(out, CsvPreference.STANDARD_PREFERENCE);

      if (CommonUtils.isEmpty(beanWriter)) {
        return null;
      }
      // Initialize the CSV header names.
      final String[] header =
        { Constants.LOCATION_ID, Constants.LOCATION_NAME, Constants.LOCATION_TYPE,
          Constants.LOCATION_LATITUDE, Constants.LOCATION_LONGITUDE };
      beanWriter.writeHeader(header);
      // Iterate over list and write using csv writer.
      for (final LocationMapper aBook : locationMapperLst) {
        beanWriter.write(aBook, header, processors);
      }

    } finally {
      if (beanWriter != null) {
        beanWriter.close();
      }
    }
    return finalCsvDirectory + csvFileName;

  }
}
