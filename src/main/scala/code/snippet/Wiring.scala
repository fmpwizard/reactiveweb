package com.fmpwizard.code.snippet

/*
 * Copyright 2007-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import _root_.scala.xml._
import _root_.net.liftweb._
import http._
import S._
import SHtml._
import common._
import util._
import Helpers._
import js._
import JsCmds._
import js.jquery._

class Wiring {

  var state= CitiesAndStates.state
  var city= ""
  var id= "1"

  def stateDropDown = SHtml.ajaxSelect(
                  CitiesAndStates.states.map(i => (i, i)),
                  Full(1.toString),
                  selected => {
                    //What to do when you select an entry
                    Info.selectedState.set(selected)
                    state= selected
                    Noop
                  }
                  )

  def cityDropDown(in: NodeSeq) =
    WiringUI.toNode(in, Info.cities, JqWiringSupport.fade)((d, ns) => cityChoice(state))

  def idDropDown(in: NodeSeq) =
    WiringUI.toNode(in, Info.ids, JqWiringSupport.fade)((d, ns) => idChoice(state))

  private object Info {
    val selectedState= ValueCell(state)
    val cities= selectedState.lift(_ + "")
    val ids= selectedState.lift(_ + "")
  }
  /**
   * Generate the City drop down menu
   */
  private def cityChoice(state: String): Elem = {
    val cities = CitiesAndStates.citiesFor(state)
    val first = cities.head
    // make the select "untrusted" because we might put new values
    // in the select
    untrustedSelect(cities.map(s => (s,s)), Full(first), s => city = s)
  }

  private def idChoice(state: String): Elem = {
    val ids = CitiesAndStates.idsFor(state)
    val first = ids.headOption
    // make the select "untrusted" because we might put new values
    // in the select
    untrustedSelect(ids.map(s => (s,s)), first, s => id = s)
  }



  // bind the view to the dynamic HTML
  def show(xhtml: Group): NodeSeq = {
    bind("select", xhtml,
         "city" -> cityChoice(state) % ("id" -> "city_select"),
         "submit" -> submit(?("Save"),
                            () =>
                            {S.notice("City: "+city+" State: "+state);
                             redirectTo("/")}))
  }
}

object CitiesAndStates extends Logger {
  val citiesAndStates = List("Alabama" -> "Birmingham",
                             "Alabama" -> "Huntsville",
                             "Alabama" -> "Mobile",
                             "Alabama" -> "Montgomery",
                             "Alaska" -> "Anchorage municipality",
                             "Arizona" -> "Chandler",
                             "Arizona" -> "Gilbert town",
                             "Arizona" -> "Glendale",
                             "Arizona" -> "Mesa",
                             "Arizona" -> "Peoria",
                             "Arizona" -> "Phoenix",
                             "Arizona" -> "Scottsdale",
                             "Arizona" -> "Tempe",
                             "Arizona" -> "Tucson",
                             "Arkansas" -> "Little Rock",
                             "California" -> "Anaheim",
                             "California" -> "Antioch",
                             "California" -> "Bakersfield",
                             "California" -> "Berkeley",
                             "California" -> "Burbank",
                             "California" -> "Chula Vista",
                             "California" -> "Concord",
                             "California" -> "Corona",
                             "California" -> "Costa Mesa",
                             "California" -> "Daly City",
                             "California" -> "Downey",
                             "California" -> "El Monte",
                             "California" -> "Elk Grove",
                             "California" -> "Escondido",
                             "California" -> "Fairfield",
                             "California" -> "Fontana",
                             "California" -> "Fremont",
                             "California" -> "Fresno",
                             "California" -> "Fullerton",
                             "California" -> "Garden Grove",
                             "California" -> "Glendale",
                             "California" -> "Hayward",
                             "California" -> "Huntington Beach",
                             "California" -> "Inglewood",
                             "California" -> "Irvine",
                             "California" -> "Lancaster",
                             "California" -> "Long Beach",
                             "California" -> "Los Angeles",
                             "California" -> "Modesto",
                             "California" -> "Moreno Valley",
                             "California" -> "Norwalk",
                             "California" -> "Oakland",
                             "California" -> "Oceanside",
                             "California" -> "Ontario",
                             "California" -> "Orange",
                             "California" -> "Oxnard",
                             "California" -> "Palmdale",
                             "California" -> "Pasadena",
                             "California" -> "Pomona",
                             "California" -> "Rancho Cucamonga",
                             "California" -> "Richmond",
                             "California" -> "Riverside",
                             "California" -> "Roseville",
                             "California" -> "Sacramento",
                             "California" -> "Salinas",
                             "California" -> "San Bernardino",
                             "California" -> "San Buenaventura (Ventura)",
                             "California" -> "San Diego",
                             "California" -> "San Francisco",
                             "California" -> "San Jose",
                             "California" -> "Santa Ana",
                             "California" -> "Santa Clara",
                             "California" -> "Santa Clarita",
                             "California" -> "Santa Rosa",
                             "California" -> "Simi Valley",
                             "California" -> "Stockton",
                             "California" -> "Sunnyvale",
                             "California" -> "Thousand Oaks",
                             "California" -> "Torrance",
                             "California" -> "Vallejo",
                             "California" -> "Visalia",
                             "California" -> "West Covina",
                             "Colorado" -> "Arvada",
                             "Colorado" -> "Aurora",
                             "Colorado" -> "Colorado Springs",
                             "Colorado" -> "Denver",
                             "Colorado" -> "Fort Collins",
                             "Colorado" -> "Lakewood",
                             "Colorado" -> "Pueblo",
                             "Colorado" -> "Thornton",
                             "Colorado" -> "Westminster",
                             "Connecticut" -> "Bridgeport",
                             "Connecticut" -> "Hartford",
                             "Connecticut" -> "New Haven",
                             "Connecticut" -> "Stamford",
                             "Connecticut" -> "Waterbury",
                             "District of Columbia" -> "Washington",
                             "Florida" -> "Cape Coral",
                             "Florida" -> "Clearwater",
                             "Florida" -> "Coral Springs",
                             "Florida" -> "Fort Lauderdale",
                             "Florida" -> "Gainesville",
                             "Florida" -> "Hialeah",
                             "Florida" -> "Hollywood",
                             "Florida" -> "Jacksonville",
                             "Florida" -> "Miami",
                             "Florida" -> "Miramar",
                             "Florida" -> "Orlando",
                             "Florida" -> "Pembroke Pines",
                             "Florida" -> "Pompano Beach",
                             "Florida" -> "Port St. Lucie",
                             "Florida" -> "St. Petersburg",
                             "Florida" -> "Tallahassee",
                             "Florida" -> "Tampa",
                             "Georgia" -> "Athens-Clarke County (balance)",
                             "Georgia" -> "Atlanta",
                             "Georgia" -> "Augusta-Richmond County (balance)",
                             "Georgia" -> "Columbus",
                             "Georgia" -> "Savannah",
                             "Hawaii" -> "Honolulu CDP",
                             "Idaho" -> "Boise City",
                             "Illinois" -> "Aurora",
                             "Illinois" -> "Chicago",
                             "Illinois" -> "Elgin",
                             "Illinois" -> "Joliet",
                             "Illinois" -> "Naperville",
                             "Illinois" -> "Peoria",
                             "Illinois" -> "Rockford",
                             "Illinois" -> "Springfield",
                             "Indiana" -> "Evansville",
                             "Indiana" -> "Fort Wayne",
                             "Indiana" -> "Indianapolis city (balance)",
                             "Indiana" -> "South Bend",
                             "Iowa" -> "Cedar Rapids",
                             "Iowa" -> "Des Moines",
                             "Kansas" -> "Kansas City",
                             "Kansas" -> "Olathe",
                             "Kansas" -> "Overland Park",
                             "Kansas" -> "Topeka",
                             "Kansas" -> "Wichita",
                             "Kentucky" -> "Lexington-Fayette",
                             "Kentucky" -> "Louisville/Jefferson County (balance)",
                             "Louisiana" -> "Baton Rouge",
                             "Louisiana" -> "Lafayette",
                             "Louisiana" -> "New Orleans",
                             "Louisiana" -> "Shreveport",
                             "Maryland" -> "Baltimore",
                             "Massachusetts" -> "Boston",
                             "Massachusetts" -> "Cambridge",
                             "Massachusetts" -> "Lowell",
                             "Massachusetts" -> "Springfield",
                             "Massachusetts" -> "Worcester",
                             "Michigan" -> "Ann Arbor",
                             "Michigan" -> "Detroit",
                             "Michigan" -> "Flint",
                             "Michigan" -> "Grand Rapids",
                             "Michigan" -> "Lansing",
                             "Michigan" -> "Sterling Heights",
                             "Michigan" -> "Warren",
                             "Minnesota" -> "Minneapolis",
                             "Minnesota" -> "St. Paul",
                             "Mississippi" -> "Jackson",
                             "Missouri" -> "Independence",
                             "Missouri" -> "Kansas City",
                             "Missouri" -> "Springfield",
                             "Missouri" -> "St. Louis",
                             "Montana" -> "Billings",
                             "Nebraska" -> "Lincoln",
                             "Nebraska" -> "Omaha",
                             "Nevada" -> "Henderson",
                             "Nevada" -> "Las Vegas",
                             "Nevada" -> "North Las Vegas",
                             "Nevada" -> "Reno",
                             "New Hampshire" -> "Manchester",
                             "New Jersey" -> "Elizabeth",
                             "New Jersey" -> "Jersey City",
                             "New Jersey" -> "Newark",
                             "New Jersey" -> "Paterson",
                             "New Mexico" -> "Albuquerque",
                             "New York" -> "Buffalo",
                             "New York" -> "New York",
                             "New York" -> "Rochester",
                             "New York" -> "Syracuse",
                             "New York" -> "Yonkers",
                             "North Carolina" -> "Cary town",
                             "North Carolina" -> "Charlotte",
                             "North Carolina" -> "Durham",
                             "North Carolina" -> "Fayetteville",
                             "North Carolina" -> "Greensboro",
                             "North Carolina" -> "Raleigh",
                             "North Carolina" -> "Winston-Salem",
                             "Ohio" -> "Akron",
                             "Ohio" -> "Cincinnati",
                             "Ohio" -> "Cleveland",
                             "Ohio" -> "Columbus",
                             "Ohio" -> "Dayton",
                             "Ohio" -> "Toledo",
                             "Oklahoma" -> "Norman",
                             "Oklahoma" -> "Oklahoma City",
                             "Oklahoma" -> "Tulsa",
                             "Oregon" -> "Eugene",
                             "Oregon" -> "Portland",
                             "Oregon" -> "Salem",
                             "Pennsylvania" -> "Allentown",
                             "Pennsylvania" -> "Erie",
                             "Pennsylvania" -> "Philadelphia",
                             "Pennsylvania" -> "Pittsburgh",
                             "Rhode Island" -> "Providence",
                             "South Carolina" -> "Charleston",
                             "South Carolina" -> "Columbia",
                             "South Dakota" -> "Sioux Falls",
                             "Tennessee" -> "Chattanooga",
                             "Tennessee" -> "Clarksville",
                             "Tennessee" -> "Knoxville",
                             "Tennessee" -> "Memphis",
                             "Tennessee" -> "Nashville-Davidson (balance)",
                             "Texas" -> "Abilene",
                             "Texas" -> "Amarillo",
                             "Texas" -> "Arlington",
                             "Texas" -> "Austin",
                             "Texas" -> "Beaumont",
                             "Texas" -> "Brownsville",
                             "Texas" -> "Carrollton",
                             "Texas" -> "Corpus Christi",
                             "Texas" -> "Dallas",
                             "Texas" -> "Denton",
                             "Texas" -> "El Paso",
                             "Texas" -> "Fort Worth",
                             "Texas" -> "Garland",
                             "Texas" -> "Grand Prairie",
                             "Texas" -> "Houston",
                             "Texas" -> "Irving",
                             "Texas" -> "Killeen",
                             "Texas" -> "Laredo",
                             "Texas" -> "Lubbock",
                             "Texas" -> "McAllen",
                             "Texas" -> "McKinney",
                             "Texas" -> "Mesquite",
                             "Texas" -> "Midland",
                             "Texas" -> "Pasadena",
                             "Texas" -> "Plano",
                             "Texas" -> "San Antonio",
                             "Texas" -> "Waco",
                             "Utah" -> "Provo",
                             "Utah" -> "Salt Lake City",
                             "Utah" -> "West Valley City",
                             "Virginia" -> "Alexandria",
                             "Virginia" -> "Arlington CDP",
                             "Virginia" -> "Chesapeake",
                             "Virginia" -> "Hampton",
                             "Virginia" -> "Newport News",
                             "Virginia" -> "Norfolk",
                             "Virginia" -> "Portsmouth",
                             "Virginia" -> "Richmond",
                             "Virginia" -> "Virginia Beach",
                             "Washington" -> "Bellevue",
                             "Washington" -> "Seattle",
                             "Washington" -> "Spokane",
                             "Washington" -> "Tacoma",
                             "Washington" -> "Vancouver",
                             "Wisconsin" -> "Green Bay",
                             "Wisconsin" -> "Madison",
                             "Wisconsin" -> "Milwaukee")


  val idsAndStates =    List("Alabama" -> "1",
                             "Alaska" -> "2",
                             "Arizona" -> "3",
                             "Arkansas" -> "4",
                             "California" -> "5",
                             "Colorado" -> "6",
                             "Connecticut" -> "7",
                             "District of Columbia" -> "8",
                             "Florida" -> "9",
                             "Georgia" -> "10",
                             "Hawaii" -> "11",
                             "Idaho" -> "12",
                             "Illinois" -> "13",
                             "Indiana" -> "14",
                             "Iowa" -> "15",
                             "Kansas" -> "16",
                             "Kentucky" -> "17",
                             "Louisiana" -> "18",
                             "Maryland" -> "19",
                             "Massachusetts" -> "20",
                             "Michigan" -> "21",
                             "Minnesota" -> "22",
                             "Mississippi" -> "23",
                             "Missouri" -> "24",
                             "Montana" -> "25",
                             "Nebraska" -> "26",
                             "Nevada" -> "27",
                             "New Hampshire" -> "28",
                             "New Jersey" -> "29",
                             "New Mexico" -> "Albuquerque",
                             "New York" -> "30",
                             "North Carolina" -> "31",
                             "Ohio" -> "32",
                             "Oklahoma" -> "33",
                             "Oregon" -> "34",
                             "Pennsylvania" -> "35",
                             "Rhode Island" -> "36",
                             "South Carolina" -> "37",
                             "South Dakota" -> "38",
                             "Tennessee" -> "39",
                             "Texas" -> "40",
                             "Utah" -> "41",
                             "Virginia" -> "42",
                             "Washington" -> "43",
                             "Wisconsin" -> "44")


  val states = citiesAndStates.map(_._1).distinct

  val state: String = states.head

  def citiesFor(state: String): List[String] = citiesAndStates.filter(_._1 == state).map(_._2)
  def idsFor(state: String): List[String] = {
    info("State is :%s".format(state))
    idsAndStates.filter(_._1 == state).map(_._2)
  }
}
