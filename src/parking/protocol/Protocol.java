package parking.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.helpers.Util;

public class Protocol implements IProtocol {

	public enum PaymentMethod {
		BY_ENTRY, BY_HOUR;

		public static PaymentMethod fromOrdinal(int num) {
			try {
				return PaymentMethod.values()[num];
			} catch (ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}
	}

	public static byte[] getUserIDCommand() {
		return new byte[] { con_start, con_cmd_get, con_nam_uid, con_end };
	}

	public static byte[] getUnknownCommand() {
		return new byte[] { con_unknown };
	}

	public static byte[] getConfirmCommand() {
		return new byte[] { con_ok };
	}

	public static byte[] getSetNewRegistryCommand(int parkingID, String parkingName,
			int entryId, Date entryTime, PaymentMethod paymentMethod,
			float parkingFee) {

		//Initialize with start
		byte[] ret = new byte[] { con_start };			
		
		//Limit String
		if (parkingName.length() > 25)
		{
			parkingName = parkingName.substring(0, 24);
		}
		
		byte[] sParkingID = ByteBuffer.allocate(4).putInt(parkingID).array();
		byte[] sParkingName = ByteBuffer.allocate(25).put(parkingName.getBytes()).array();
		byte[] sEntryId = ByteBuffer.allocate(4).putInt(entryId).array();		
		byte[] sEntryTime = ByteBuffer.allocate(8).putLong(entryTime.getTime()).array();		
		byte[] sPaymentMethod = ByteBuffer.allocate(4).putInt(paymentMethod.ordinal()).array();
		byte[] sParkingFee = ByteBuffer.allocate(4).putFloat(parkingFee).array();
		
		//Add Parking Id
		ret = ArrayUtils.addAll(ret, sParkingID);
		
		//Add Get Command
		ret = ArrayUtils.add(ret, con_cmd_set);
		
		//Add New Registry Name
		ret = ArrayUtils.add(ret, con_nam_new_entry);
		
		//Add Parking Name
		ret = ArrayUtils.addAll(ret, sParkingName);
		
		//Add Entry Id
		ret = ArrayUtils.addAll(ret, sEntryId);
		
		//Add Entry Time
		ret = ArrayUtils.addAll(ret, sEntryTime);
		
		//Add Payment Method
		ret = ArrayUtils.addAll(ret, sPaymentMethod);
		
		//Add Parking Fee
		ret = ArrayUtils.addAll(ret, sParkingFee);
		
		//Add New Registry Name
		ret = ArrayUtils.add(ret, con_end);
		
		return ret;
	}

}