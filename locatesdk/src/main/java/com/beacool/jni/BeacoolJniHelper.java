package com.beacool.jni;

public class BeacoolJniHelper {
	/**
	 * @Function: Init the Algorithm.
	 * 
	 * @NOTE: THIS function need to called when we start the positioning, even
	 *        if we just pause the algorithm for a while and restart, we also
	 *        need to call this function.
	 * 
	 * @param index
	 *            -- Muti-algorithm running simultaneously, with this Index to
	 *            Identify, normally we just set this to 0.
	 * 
	 */
	public static native void CYWBBPositionInit(int index, int initFloor,
			float initX, float initY,float initAccRate);

	/**
	 * @Function: Update the ACC data to the algorithm.
	 * 
	 * @NOTE: THIS function need to called when get a ACC row data every 20 ms.
	 * 
	 * @param acc_x
	 *            -- Acc row data in, with the unit "g", for example, 1.0 means
	 *            1g.
	 * @param acc_y
	 *            -- Acc row data in, with the unit "g", for example, 1.0 means
	 *            1g.
	 * @param acc_z
	 *            -- Acc row data in, with the unit "g", for example, 1.0 means
	 *            1g.
	 */
	public static native void CYWAccUpdatePoll(float acc_x, float acc_y,
			float acc_z);

	/**
	 * @Function: Get user postion according to the Beacon input.
	 * @NOTE: THIS function need to called when get a group of Beacon
	 *        informations.For APPLE current system, we can get a group beacon
	 *        information every 1 second and call this interface.
	 * 
	 * @param index
	 *            -- Muti-algorithm running simultaneously, with this Index to
	 *            Identify, normally we just set this to 0.
	 * @param beaconFNUM
	 *            -- beacon number to the algorithm.
	 * @param major
	 * @param minor
	 * @param rssi
	 * @param distance
	 * @param beaconpos_x
	 * @param beaconpos_y
	 * @param floor
	 * @param comAngle
	 * @param floorOut
	 * @param targetPos_x
	 * @param targetPos_y
	 * 
	 * @Output: targetPos_x, *targetPos_y -- Position of the user who hold the
	 *          mobile phone; floorOut -- floor of the user who hold the mobile
	 *          phone.
	 * 
	 * @return -1: Not valid; 0: Valid
	 */
	public static native int CYWBeaconPositionPoll(int index, int beaconFNUM,
			short major[], short minor[], float rssi[], float distance[],
			float beaconpos_x[], float beaconpos_y[], int floor[],
			float comAngle);

	public static native int CYWGetFloorOut(int index);

	public static native float CYWGetPosXOut(int index);

	public static native float CYWGetPosYOut(int index);

}
