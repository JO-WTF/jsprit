/*
 * Licensed to GraphHopper GmbH under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * GraphHopper GmbH licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.graphhopper.jsprit.core.util;

@Deprecated
public class EuclideanDistanceCalculator {

    /**
     * deprecated - use distance/EuclideanDistanceCalculator instead
     *
     * @param from
     * @param to
     * @return
     */
    @Deprecated
    public static double calculateDistance(Coordinate from, Coordinate to) {
        double xDiff = from.getX() - to.getX();
        double yDiff = from.getY() - to.getY();
        return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }

}