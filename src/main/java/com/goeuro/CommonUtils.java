/**
 * Copyright (C) 2016 Gurpreet Singh (preet.tech89@gmail.com)
 *
 * Modification Log
 * Date          Author                                         Remarks
 * Apr 22,2016   Gurpreet Singh (preet.tech89@gmail.com)        Created
 *
 */

package com.goeuro;

import java.util.Collection;

/**
 * Common Utility class used in various null or equality and other basic functionality required in
 * application implementation.
 * 
 * @author Gurpreet Singh preet.tech89@gmail.com
 */
public class CommonUtils {

  /**
   * Checks to see if the given Collection is null or contains no object.
   * 
   * @param <T>
   * 
   * @param val passed Collection object
   * @return boolean true or false.
   */
  public static <T> boolean isEmpty(final Collection<T> val) {
    return val == null || (val.size() == 0);
  }

  /**
   * Checks to see if the given Object is null or not.
   * 
   * @param obj object to be checked
   * @return boolean true or false.
   */
  public static boolean isEmpty(final Object obj) {
    if (obj == null) {
      return true;
    }
    if (obj instanceof String) {
      return isEmpty((String) obj);
    }
    return false;
  }

  /**
   * Checks to see if the given String is null or blank.
   * 
   * @param val passed String object
   * @return boolean true or false.
   */
  public static boolean isEmpty(final String val) {
    return val == null || val.trim().equals("");
  }

  /**
   * Checks whether the given Array is null or contain no element.
   * 
   * @param val this is the passed String Array
   * @return boolean true or false.
   */
  public static boolean isEmpty(final String[] val) {
    return val == null || val.length == 0;
  }

}
