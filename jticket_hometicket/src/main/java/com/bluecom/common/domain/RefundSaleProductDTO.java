
package com.bluecom.common.domain;
import java.io.Serializable;

import lombok.Data;

@Data
public class RefundSaleProductDTO extends BaseDTO implements Serializable{

	String saleShopCode = "";
	String saleCode = "";
	String saleSequence = "";
	String ticketControlNo = "";
	String productShopCode = "";
	String productCode = "";
	String productName = "";
	String packageYn = "";
	int packageIdx = 0;
	String bookYn = "";
	String bookNo = "";
	String quantity = "";
	String playDate = "";
	String playedDate = "";
	String playSequence = "";
	String productFee = "";
	String refundFee = "";
	String seasonCode = "";
	String memberDiscountYn = "";
	String seatCode = "";
	String scheduleCode = "";
	String workDatetime = null;
	String checkInYn = "";
	String checkInUserId = "";
	String checkInTerminalCode = "";
	String checkInDatetime = null;
	String printYn = "";
	String printUserId = "";
	String printTerminalCode = "";
	String printDatetime = null;
	String refundYn = "";
	String refundUserId = "";
	String refundDatetime = null;
	String refundTerminalCode = "";
	String personName = "";
	String personMobileNo = "";
	String personJumin = "";
	String personAddr = "";
	String boardingNum = "";
	String channelType = "";
	String unitPrice = "";
	String schduleText = "";
	String expireDate;
	String qrCode = "";
	
}
