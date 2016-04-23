/**
 * Copyright (C) 2016 Gurpreet Singh (preet.tech89@gmail.com)
 *
 * Modification Log
 * Date          Author                                         Remarks
 * Apr 22,2016   Gurpreet Singh (preet.tech89@gmail.com)        Created
 *
 */

package com.goeuro;

/**
 * LocationMapper Model class used in binding the data of Location.
 * 
 * @author Gurpreet Singh preet.tech89@gmail.com
 */
public class LocationMapper {

  /**
   * _id holds the location id reference.
   */
  private int _id;

  /**
   * name holds the location name reference.
   */
  private String name;

  /**
   * type holds the location type reference.
   */
  private String type;

  /**
   * latitude holds the location latitude reference.
   */
  private Double latitude;

  /**
   * longitude holds the location longitude reference.
   */
  private Double longitude;

  /**
   * Constructs LocationMapper.
   */
  public LocationMapper() {
  }

  /**
   * Constructs LocationMapper.
   * 
   * @param id holds the location id.
   * @param name holds the location name.
   * @param type holds the location type.
   * @param latitude holds the location latitude.
   * @param longitude holds the location longitude.
   */
  public LocationMapper(final int id, final String name, final String type, final Double latitude,
      final Double longitude) {
    _id = id;
    this.name = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * @return the _id
   */
  public int get_id() {
    return _id;
  }

  /**
   * @return the latitude
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * @return the longitude
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param _id the _id to set
   */
  public void set_id(final int _id) {
    this._id = _id;
  }

  /**
   * @param latitude the latitude to set
   */
  public void setLatitude(final Double latitude) {
    this.latitude = latitude;
  }

  /**
   * @param longitude the longitude to set
   */
  public void setLongitude(final Double longitude) {
    this.longitude = longitude;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @param type the type to set
   */
  public void setType(final String type) {
    this.type = type;
  }

}
