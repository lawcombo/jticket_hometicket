<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div class="personal_wrap">
	<ul class="utill_ul">
		<li class="personal">
			<a href="#">개인정보처리방침</a>
			<div class="modal_bx">
				<div class="modal_content person">
					<p class="modal_close"><a href="#"></a></p>
					<div class="modal_txt">
					 	<h3>개인정보처리방침</h3>
					 	<div class="cont_txtbx01">
					 		<p class="cont_txts">${shopDetail.shop_name } (ibooking.nicetcm.co.kr 이하 "회사" 라고 함)은 정보통신망 이용촉진 및 정보보호 등에 관한 법률, 개인정보보호법, 전자금융거래법 등 회사가 준수하여야 할 관련 법령상의 개인정보보호 규정을 준수하며 관련 법령에 의거한 개인정보처리방침을 정하여 이용자 권익 보호에 최선을 다하고 있습니다.
회사가 처리하는 모든 개인정보는 관련 법령에 근거하거나 정보주체의 동의하에 수집ㆍ보유 및 처리하고 있습니다. 본 개인정보처리방침은 회사가 제공하는 서비스 이용, 입사지원과 관련된 내용에 적용되며 다음과 같은 내용을 담고 있습니다.</p>
							<p class="cont_tit1">1. 개인정보의 수집•이용 목적, 수집하는 개인정보의 항목 및 수집방법</p>
							<p class="cont_tit2">가) 개인정보의 수집•이용 목적</p>
							<p class="cont_txts pad_left">· 서비스 제공 : CD VAN, ATM 관리서비스(이용, 고객문의, CD기 설치문의, 고객민원 처리), 키오스크 서비스(설치문의, 고객민원처리, 가맹점), 무인주차장 관리 서비스(이용, 고객민원 처리)</p>
							<p class="cont_txts pad_left">· 입사지원 : 회사의 공개, 특별 채용 시 입사전형, 자격증빙, 지원자와의 원활한 의사소통</p>
							<p class="cont_tit2">나) 수집하는 개인정보의 항목</p>
							<div class="person_table">
								<table class="per_table">
									<colgroup>
										<col class="percol01">
										<col class="percol02">
										<col class="percol03">
									</colgroup>
									<thead>
										<tr>
											<th>서비스명</th>
											<th>필수항목</th>
											<th>선택항목</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>고객문의</td>
											<td>성명, 이메일주소, 전화번호</td>
											<td></td>
										</tr>
										<tr>
											<td>사이버민원</td>
											<td>성명, 이메일주소, 전화번호</td>
											<td></td>
										</tr>
										<tr>
											<td>채용문의</td>
											<td>성명, 이메일주소, 전화번호</td>
											<td></td>
										</tr>
										<tr>
											<td>설치문의</td>
											<td>성명, 이메일주소, 전화번호, 사업장 위치 및 주변환경</td>
											<td></td>
										</tr>
										<tr>
											<td>고객민원</td>
											<td>성명, 전화번호</td>
											<td>계좌번호, 카드번호</td>
										</tr>
										<tr>
											<td>CD VAN 서비스 이용</td>
											<td>계좌정보, 카드번호, 거래 일시, 거래 금액 등 전자금융거래기록<br>(예금거래, 현금서비스거래, 무매체거래, 통장거래 등)</td>
											<td></td>
										</tr>
										<tr>
											<td>무인주차장 관리 서비스 이용</td>
											<td>(공통)차량번호, 카드번호, 거래일시 등 (정기)차량번호, 성명, 전화번호</td>
											<td></td>
										</tr>
										<tr>
											<td>키오스크</td>
											<td>가맹점 정보(상호, 사업자등록번호, 주소, 연락처 등)</td>
											<td></td>
										</tr>
										<tr>
											<td>입사지원</td>
											<td>성명, 이메일, 생년월일, 연락처, 주소, 학력 및 경력, 병역의무 등 채용에 필요한 사항</td>
											<td>보훈여부, 장애여부, 영문성명 등</td>
										</tr>
									</tbody>
								</table>
							</div><!-- person_table end -->
							<br>
							<p class="cont_tit2">※ 위의 정보 외 서비스 이용과정이나 서비스 처리과정에서 아래와 같은 정보들이 자ㆍ수동으로 생성되어 수집될 수 있습니다.<br>- 이용자 ip 주소, 서비스 접속일시, 서비스 이용기록, 불량, 오류 혹은 비정상 이용기록, 전자금융거래기록 등</p>
							<p class="cont_tit2">다) 개인정보의 수집 방법</p>
							<p class="cont_txts pad_left">· 홈페이지 및 이메일, 전화, 팩스, 서면 등</p>
							<p class="cont_tit1">2. 개인정보의 보유 및 이용 기간</p>
							<p class="cont_tit2">개인정보는 원칙적으로 개인정보의 수집 및 이용목적이 달성되면 지체 없이 파기합니다. 단, 관계 법령의 규정에 의하여 보존할 필요가 있는 경우 회사는 아래와 같이 관계법령에서 정한 일정한 기간이나 정보주체로부터 동의받은 범위내에서 보유합니다.</p>
					 		<p class="cont_txts pad_left">· 건당 1만원 초과 전자금융거래에 관한 기록<br> - 보존 이유 : 전자금융거래법 <bR> - 보존 기간 : 5년</p>
					 		<p class="cont_txts pad_left">· 건당 1만원 이하 전자금융거래에 관한 기록<br> - 보존 이유 : 전자금융거래법 <bR> - 보존 기간 : 1년</p>
					 		<p class="cont_txts pad_left">· 계약 또는 청약 철회 등에 관한 기록<br> - 보존 이유 : 전자상거래 등에서의 소비자보호에 관한 법률 <bR> - 보존 기간 : 5년</p>
					 		<p class="cont_txts pad_left">· 대금결제 및 재화 등의 공급에 관한 기록<br> - 보존 이유 : 전자상거래 등에서의 소비자보호에 관한 법률 <bR> - 보존 기간 : 5년</p>
					 		<p class="cont_txts pad_left">· 소비자의 불만 또는 분쟁처리에 관한 기록<br> - 보존 이유 : 전자상거래 등에서의 소비자보호에 관한 법률 <bR> - 보존 기간 : 3년</p>
					 		<p class="cont_txts pad_left">· 본인확인에 관한 기록<br> - 보존 이유 : 정보통신망 이용촉진 및 정보보호 등에 관한 법률 <bR> - 보존 기간 : 6개월</p>
					 		<p class="cont_txts pad_left">· 입사지원에 관한 기록<br> - 보존 이유 <bR>: 입사 전형 진행, 입사 지원서 수정, 합격여부 확인, 입사 지원자와의 원활한 의사소통 <bR>: (최종합격시) 인사관리, 근로계약 체결/이행, 근로자 명부 및 임금대장 작성 등<br>- 보존 기간 : 목적 달성 시 즉시 파기</p>
					 		<p class="cont_tit1">3. 개인정보의 제공</p>
							<p class="cont_tit2">회사는 고객문의, CD VAN 설치문의 이용고객, 온라인 입사지원 등 수집된 개인정보를 목적 외 사용 및 제 3 자에게 제공하거나 공유하지 않습니다. 다만, 다음의 경우에는 예외로 합니다.</p>
					 		<p class="cont_txts pad_left">· 정보주체로부터 별도의 동의를 받은 경우</p>
					 		<p class="cont_txts pad_left">· 법률에 특별한 규정이 있거나 법령상 의무를 준수하기 위하여 불가피한 경우</p>
					 		<p class="cont_txts pad_left">· 정보주체 또는 그 법정대리인이 의사표시를 할 수 없는 상태에 있거나 주소불명 등으로 사전 동의를 받을 수 없는 경우로서 명백히 정보주체 또는 제3자의 급박한 생명, 신체, 재산의 이익을 위하여 필요하다고 인정되는 경우</p>
					 		<p class="cont_txts pad_left">· 통계작성 및 학술연구 등의 목적을 위하여 필요한 경우로써 특정 개인을 알아볼 수 없는 형태로 개인정보를 제공하는 경우</p>
					 		<p class="cont_tit2">회사는 향후 개인정보를 제3자에게 제공하는 경우, 다음의 항목을 정보주체에게 알린 후 동의를 받도록 하겠습니다.</p>
					 		<p class="cont_txts pad_left">· 제공받는 자의 성명(법인 또는 단체인 경우에는 그 명칭)과 연락처</p>
					 		<p class="cont_txts pad_left">· 제공받는 자의 개인정보 이용 목적, 제공하는 개인정보의 항목</p>
					 		<p class="cont_txts pad_left">· 개인정보를 제공받는 자의 개인정보 보유 및 이용 기간</p>
					 		<p class="cont_txts pad_left">· 동의를 거부할 권리가 있다는 사실 및 동의 거부에 따른 불이익이 있는 경우에는 그 불이익의 내용</p>
					 		<p class="cont_tit1">4. 개인정보 처리위탁</p>
							<p class="cont_tit2">회사에서 제공하는 서비스 중 발생한 장애업무 처리 등 고객지원을 위해 아래와 같이 개인정보처리 업무를 외부업체에 위탁하고 있으며, 위탁 계약시 개인정보가 안전하게 관리될 수 있도록 필요한 사항을 규정하고 있습니다. 회사의 개인정보 위탁처리 기관 및 위탁업무 내용은 아래와 같습니다.</p>
					 		<div class="person_table tb2">
								<table class="per_table">
									<colgroup>
										<col class="per2col01">
										<col class="per2col02">
										<col class="per2col03">
									</colgroup>
									<thead>
										<tr>
											<th>수탁업체</th>
											<th>위탁업무 내용</th>
											<th>개인정보 보유 및 이용기간</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>NICE신용정보㈜</td>
											<td>한국전자금융에서 제공하는 서비스<br>- 장애업무 처리 및 상담</td>
											<td>- 성명, 전화번호, 계좌번호, 카드번호<br>- 3개월</td>
										</tr>
										<tr>
											<td>NICE씨엠에스㈜</td>
											<td>한국전자금융에서 제공하는 서비스<br>- 현송 및 정산, 매체 관리</td>
											<td>- 성명, 전화번호, 계좌번호, 카드번호<br>- 3개월</td>
										</tr>
										<tr>
											<td>S1</td>
											<td>한국전자금융에서 제공하는 서비스<br>- 장애처리</td>
											<td>- 성명, 전화번호<br>- 3개월</td>
										</tr>
										<tr>
											<td>Caps</td>
											<td>한국전자금융에서 제공하는 서비스<br>- 장애처리</td>
											<td>- 성명, 전화번호<br>- 3개월</td>
										</tr>
										<tr>
											<td>KT 텔레캅</td>
											<td>한국전자금융에서 제공하는 서비스<br>- 장애처리</td>
											<td>- 성명, 전화번호<br>- 3개월</td>
										</tr>
									</tbody>
								</table>
							</div><!-- person_table tb2 end -->
							<br>
							<p class="cont_tit1">5. 개인정보의 안전성 확보 조치</p>
							<p class="cont_tit2">회사는 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」제 28조, 「개인정보보호법」 제29조에 따라 다음과 같이 안전성 확보에 필요한 기술적, 관리적, 물리적 조치를 하고 있습니다.</p>
							<p class="cont_tit2">가) 내부관리계획의 수립 및 시행</p>
							<p class="cont_txts pad_left">· 회사는 ‘개인정보의 안전성 확보조치 기준’에 의거하여 내부관리계획을 수립 및 시행합니다.</p>
							<p class="cont_tit2">나) 개인정보취급자 지정의 최소화 및 교육</p>
							<p class="cont_txts pad_left">· 개인정보취급자의 지정을 최소화하고 정기적인 교육을 시행하고 있습니다.</p>
							<p class="cont_tit2">다) 개인정보에 대한 접근 제한</p>
							<p class="cont_txts pad_left">· 개인정보를 처리하는 시스템에 대한 접근권한의 부여, 변경, 말소를 통하여 개인정보에 대한 접근을 통제하고, 침입차단시스템과 침입방지시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있으며 개인정보취급자가 정보통신망을 통해 외부에서 개인정보처리시스템에 접속하는 경우에는 가상사설망(VPN : Virtual Private Network)을 이용하고 있습니다. 또한 개인정보의 기술적•관리적 보호조치 기준 제4조 제3항에 의거하여 권한 부여, 변경 또는 말소에 대한 내역을 기록하고, 그 기록을 최소 5년간 보관하고 있습니다.</p>
							<p class="cont_tit2">라) 접속기록의 보관 및 위변조 방지</p>
							<p class="cont_txts pad_left">· 개인정보처리시스템에 접속한 기록(웹 로그, 요약정보 등)을 최소 1년 이상 보관, 관리하고 있으며, 접속 기록이 위변조 및 도난, 분실되지 않도록 관리하고 있습니다.</p>
							<p class="cont_tit2">마) 개인정보의 암호화</p>
							<p class="cont_txts pad_left">· 이용자의 개인정보는 안전한 알고리즘을 이용하여 저장 및 관리되고 있습니다. 또한 중요한 데이터는 저장 및 전송 시 데이터 암호화 또는 안전하게 전송할 수 있는 구간(SSL 등) 등의 별도 보안기능을 사용하고 있습니다.</p>
							<p class="cont_tit2">바) 해킹 등에 대비한 기술적 대책</p>
							<p class="cont_txts pad_left">· 회사는 해킹이나 컴퓨터 바이러스 등에 의한 개인정보 유출 및 훼손을 막기 위하여 보안프로그램을 설치하고 주기적인 갱신•점검을 하며 외부로부터 접근이 통제된 구역에 시스템을 설치하고 기술적, 물리적으로 감시 및 차단하고 있습니다.</p>
							<p class="cont_tit2">사) 비인가자에 대한 출입 통제</p>
							<p class="cont_txts pad_left">· 개인정보를 보관하고 있는 개인정보시스템의 물리적 보관 장소를 별도로 두고 이에 대해 출입통제 절차를 수립, 운영하고 있습니다.</p>
							
							<p class="cont_tit1">6. 정보주체의 권리ㆍ의무 및 행사 방법</p>
							<p class="cont_tit2">이용자는 정보주체로서 다음과 같은 권리를 행사할 수 있습니다.</p>
							<p class="cont_tit2">가) 개인정보 열람 요구</p>
							<p class="cont_txts pad_left">· 회사에서 보유하고 있는 개인정보파일은 「개인정보보호법」 제35조(개인정보의 열람)에 따라 열람을 요구할 수 있습니다. 다만 개인정보 열람 요구는 「개인정보보호법」 제35조 제4항에 의하여 다음과 같이 제한될 수 있습니다.</p>
					 		<p class="cont_txts pad_left2">① 법률에 따라 열람이 금지되거나 제한되는 경우</p>
					 		<p class="cont_txts pad_left2">② 다른 사람의 생명•신체를 해할 우려가 있거나 다른 사람의 재산과 그 밖의 이익을 부당하게 침해할 우려가 있는 경우</p>
					 		<p class="cont_tit2">나) 개인정보 정정•삭제 요구</p>
							<p class="cont_txts pad_left">· 회사에서 보유하고 있는 개인정보에 대해서는 「개인정보보호법」 제36조(개인정보의 정정•삭제)에 따라 회사에 개인정보의 정정•삭제를 요구할 수 있습니다. 다만, 다른 법령에서 그 개인정보가 수집 대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.</p>
							<p class="cont_tit2">다) 개인정보 처리정지 요구</p>
							<p class="cont_txts pad_left">· 회사에서 보유하고 있는 개인정보에 대해서는 「개인정보보호법」 제37조(개인정보의 처리정지 등)에 따라 회사에 개인정보의 처리정지를 요구할 수 있습니다. 또한 만 14세 미만 아동의 법정대리인은 회사에 그 아동의 개인정보의 열람, 정정•삭제, 처리정지 요구를 할 수 있습니다. 다만, 개인정보 처리정지 요구 시 「개인정보보호법」 제37조제2항에 의하여 처리정지 요구가 거절될 수 있습니다.</p>
							<p class="cont_txts pad_left2">① 법률에 특별한 규정이 있거나 법령상 의무를 준수하기 위하여 불가피한 경우</p>
					 		<p class="cont_txts pad_left2">② 다른 사람의 생명•신체를 해할 우려가 있거나 다른 사람의 재산과 그 밖의 이익을 부당하게 침해할 우려가 있는 경우</p>
					 		<p class="cont_txts pad_left2">③ 공공기관이 개인정보를 처리하지 아니하면 다른 법률에서 정하는 소관 업무를 수행할 수 없는 경우</p>
					 		<p class="cont_txts pad_left2">④ 개인정보를 처리하지 아니하면 정보주체와 약정한 서비스를 제공하지 못하는 등 계약의 이행이 곤란한 경우로서 정보주체가 그 계약의 해지 의사를 명확하게 밝히지 아니한 경우</p>
					 		<p class="cont_txts pad_left">· 개인정보의 열람, 정정•삭제, 처리정지 요구에 대해서는 10일 이내에 해당 사항에 대한 회사의 조치를 통지 합니다. 개인정보의 열람, 정정•삭제, 처리정지 요구는 해당 부서를 통해서 가능합니다.</p>
					 		<p class="cont_txts pad_left">· 위 사항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다.</p>
					 		
					 		<p class="cont_tit1">7. 개인정보의 파기</p>
							<p class="cont_tit2">회사는 원칙적으로 개인정보의 보유기간이 경과했거나 처리목적이 달성된 경우에는 지체없이(5일 이내) 해당 개인정보를 파기합니다. 다만, 다른 법률에 따라 보존하여야 하는 경우에는 그러하지 않습니다. 파기의 절차, 기한 및 방법은 다음과 같습니다.</p>
							<p class="cont_tit2">가) 파기절차</p>
							<p class="cont_txts pad_left">·이용자가 입력한 정보는 보유기간이 경과했거나 처리목적이 달성된 후 내부 방침 및 관련 법령에 따라 파기합니다.</p>
							<p class="cont_tit2">나) 파기기한</p>
							<p class="cont_txts pad_left">·이용자의 개인정보는 개인정보의 보유기간이 경과된 경우, 개인정보의 처리 목적 달성, 파기사유 발생 등 개인정보의 처리가 불필요한 것으로 인정될 경우 즉시 파기합니다.</p>
							<p class="cont_tit2">다) 파기방법</p>
							<p class="cont_txts pad_left">·회사에서 처리하는 개인정보를 파기할 때에는 다음의 방법으로 파기 합니다.</p>
							<p class="cont_txts pad_left2">① 전자적 파일 형태인 경우 : 복원이 불가능한 방법으로 영구삭제</p>
					 		<p class="cont_txts pad_left2">② 전자적 파일의 형태 외의 기록물, 인쇄물, 서면, 그 밖의 기록매체인 경우 : 파쇄 또는 소각</p>
					 		
					 		<p class="cont_tit1">8. 개인정보 자동 수집 장치의 설치ㆍ운영 및 거부에 관한 사항</p>
							<p class="cont_tit2">가) 쿠키란?</p>
							<p class="cont_txts pad_left">·회사는 개인화되고 맞춤화된 서비스를 제공하기 위해서 이용자의 정보를 저장하고 수시로 불러오는 '쿠키(cookie)'를 사용합니다.</p>
							<p class="cont_txts pad_left">·쿠키는 웹사이트를 운영하는데 이용되는 서버가 이용자의 브라우저에게 보내는 아주 작은 텍스트 파일로 이용자 컴퓨터의 하드디스크에 저장됩니다. 이후 이용자가 웹 사이트에 방문할 경우 웹 사이트 서버는 이용자의 하드 디스크에 저장되어 있는 쿠키의 내용을 읽어 이용자의 환경설정을 유지하고 맞춤화된 서비스를 제공하기 위해 이용됩니다.</p>
							<p class="cont_txts pad_left">·쿠키는 개인을 식별하는 정보를 자동적/능동적으로 수집하지 않으며, 이용자는 언제든지 이러한 쿠키의 저장을 거부하거나 삭제할 수 있습니다.</p>
							<p class="cont_tit2">나) 회사의 쿠키 사용 목적</p>
							<p class="cont_txts pad_left">·이용자들이 방문한 회사 홈페이지에 대한 방문 및 이용형태, 접속 여부 등을 파악하여 이용자에게 적절한 서비스 제공 및 통계 데이터 추출을 위해 사용합니다.</p>
							<p class="cont_tit2">다) 쿠키의 설치/운영 및 거부</p>
							<p class="cont_txts pad_left">·이용자는 쿠키 설치에 대한 선택권을 가지고 있습니다. 따라서 이용자는 웹브라우저에서 옵션을 설정함으로써 모든 쿠키를 허용하거나, 쿠키가 저장될 때마다 확인을 거치거나, 아니면 모든 쿠키의 저장을 거부할 수도 있습니다.</p>
							<p class="cont_txts pad_left">·다만, 쿠키의 저장을 거부할 경우에는 로그인이 필요한 일부 서비스는 이용에 어려움이 있을 수 있습니다.</p>
							<p class="cont_txts pad_left">·쿠키 설치 허용 여부를 지정하는 방법(Internet Explorer의 경우)은 다음과 같습니다.</p>
							<p class="cont_txts pad_left2">① [도구] 메뉴에서 [인터넷 옵션]을 선택합니다.</p>
					 		<p class="cont_txts pad_left2">② [개인정보 탭]을 클릭합니다.</p>
					 		<p class="cont_txts pad_left2">③ [개인정보취급 수준]을 설정하시면 됩니다.</p>
					 		
					 		<p class="cont_tit1">9. 개인정보보호 최고책임자 및 담당자 연락처</p>
							<p class="cont_tit2">회사는 개인정보를 보호하고 개인정보와 관련된 사항을 처리하기 위하여 아래와 같이 개인정보 보호책임자 및 실무담당자를 지정하고 있습니다. 이에 이용자는 모든 개인정보보호 관련 민원을 개인정보보호 책임자 혹은 관리자에게 문의 및 신고하실 수 있으며 회사는 신속하게 충분한 답변 드릴 것입니다.</p>
					 		
					 		<div class="person_table tb3">
								<table class="per_table">
									<colgroup>
										<col class="per3col01 m_none">
										<col class="per3col02">
										<col class="per3col03">
										<col class="per3col04">
									</colgroup>
									<thead>
										<tr>
											<th class=" m_none">구분</th>
											<th>개인정보보호 최고책임자</th>
											<th>개인정보보호 책임자</th>
											<th>개인정보보호 관리자</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class=" m_none">성명</td>
											<td>나경현</td>
											<td>구공호</td>
											<td>이승원</td>
										</tr>
										<tr>
											<td class=" m_none">직위</td>
											<td>소장</td>
											<td>실장</td>
											<td>팀장</td>
										</tr>
										<tr>
											<td class=" m_none">부서</td>
											<td>정보기술연구소</td>
											<td>인프라기획실</td>
											<td>인프라기획실</td>
										</tr>
										<tr>
											<td class=" m_none">전화</td>
											<td>(02)2122-5416</td>
											<td>(02)2122-5431</td>
											<td>(02)2122-5468</td>
										</tr>
										<tr class=" m_none">
											<td class=" m_none">메일</td>
											<td>khna@nicetcm.co.kr</td>
											<td>kongho905@nicetcm.co.kr</td>
											<td>sj4358@nicetcm.co.kr</td>
										</tr>
									</tbody>
								</table>
							</div><!-- person_table tb3 end -->
							<br>
							<p class="cont_tit2">개인정보침해에 대한 신고나 상담이 필요하신 경우에는 아래 기관에 문의하시기 바랍니다.</p>
							<p class="cont_txts pad_left">·개인정보분쟁조정위원회 (www.kopico.go.kr / 02-2100-2499)</p>
							<p class="cont_txts pad_left">·개인정보침해신고센터 (privacy.kisa.or.kr / 국번없이 118)</p>
							<p class="cont_txts pad_left">·대검찰청 사이버범죄수사단 (www.spo.go.kr / 02-3480-3571)</p>
							<p class="cont_txts pad_left">·경찰청 사이버안전국 (cyberbureau.police.go.kr / 국번없이 182)</p>
							
							<p class="cont_tit1">10. 개인정보처리방침의 변경</p>
							<p class="cont_tit2">현 개인정보처리방침 내용 추가, 삭제 및 수정이 있을 시에는 개정 최소 7일전부터 홈페이지의 '공지사항'을 통해 고지할 것입니다. 다만, 개인정보의 수집 및 활용, 제3자 제공 등과 같이 이용자 권리의 중요한 변경이 있을 경우에는 최소 30일 전에 고지합니다.</p>
							<p class="cont_txts pad_left">·버전번호: V2.3</p>
							<p class="cont_txts pad_left">·최초 시행일자 : 2011년 9월 30일</p>
							<p class="cont_txts pad_left">·고지 일자 : 2021년 01월 01일</p>
							<p class="cont_txts pad_left">·시행 일자 : 2021년 01월 08일</p>
					 	</div>
					</div>
				</div>
				<div class="modal_bk"></div>
			</div>
		</li>
		<li class="em_bt">
			<a href="#">이메일 무단수집거부</a>
			<div class="modal_bx">
				<div class="modal_content person email">
					<p class="modal_close"><a href="#"></a></p>
					<div class="modal_txt">
					 	<h3>이메일 무단수집 거부</h3>
					 	<div class="email_wrap">
							<div class="email_bx">
								<img src="${pageContext.request.contextPath }/resources/images/email_icon.png">
								<p class="email_tit">나이스티켓은 이메일 무단수집을 거부합니다.</p>
								<p class="email_txt">본 웹사이트에 게시된 이메일 주소가 전자우편 수집 프로그램이나 그밖의 기술적인 장치를 이용하여<br> 무단으로 수집되는 것을 거부하며,이를 위반시 정보통신망법에 의해 형사처벌됨을 유념하시기 바랍니다.</p>
								<p class="email_txt">게시일 : 2021년 7월 27일</p>
							</div>
						</div>
					</div>
				</div>
				<div class="modal_bk"></div>
			</div>
		</li>
		<!-- <li>
			<a href="/privatePolicy" onclick="alert('준비중입니다.'); return false;">이용약관</a>
		</li> -->
	</ul>
	
	<ul class="footer_wrap">	
		<li><c:out value="${shopDetail.address1 }${' '}${shopDetail.address2 }${' '}${shopDetail.shop_name }" /></li>
		<li class="footer_litxt"><span>대표자 : <c:out value="${shopDetail.master_name }" /></span>&nbsp;&nbsp;&nbsp;|</li>
		<li class="footer_litxt"><span>사업자번호 : <c:out value="${shopDetail.corporate_number }" /></span>&nbsp;&nbsp;&nbsp;|</li>
		<li class="footer_litxt"><span>시설전화 : <c:out value="${shopDetail.shop_tel }" /></span>&nbsp;&nbsp;&nbsp;|</li>
		<c:if test="${not empty shopDetail.shop_fax }">
			<li class="footer_litxt"><span>팩스 : <c:out value="${shopDetail.shop_fax }" /></span></li>
		</c:if>
		
	</ul>
	<ul class="footer_wrap2">
		<li class="copyrightli">© Copyright (주)한국전자금융 All rights reserved.</li>
	</ul>
</div>
<script>
$(function() {
	
	$(".personal a").click(function(){
		$(this).next(".modal_bx").fadeIn();
	});
	$(".em_bt a").click(function(){
		$(this).next(".modal_bx").fadeIn();
	});
	$(".modal_close").click(function(){
		$(".modal_bx").fadeOut();
	});
	$(".modal_bk").click(function(){
		$(".modal_bx").fadeOut();
	});
})


</script>