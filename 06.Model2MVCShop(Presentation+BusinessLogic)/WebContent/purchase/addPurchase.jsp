
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--
<%@page import="com.model2.mvc.service.domain.Purchase"%>
<% Purchase purchase = (Purchase)request.getAttribute("purchase");%>
 --%>


<html>
<head>
<title>구매완료</title>
</head>

<body>

<form name="updatePurchase" action="/updatePurchaseView.do?tranNo=0" method="post">

다음과 같이 구매가 되었습니다.

<table border=1>
	<tr>
		<td>물품번호</td>
		<td>${purchase.purchaseProd.prodNo}<%--<%= purchase.getPurchaseProd().getProdNo() --%></td>
		
		<td></td>
	</tr>
	<tr>
		<td>구매자아이디</td>
		<td>${purchase.buyer.userId}<%--<%= purchase.getBuyer().getUserId() --%></td>
		<td></td>
	</tr>
	<tr>
		<td>구매방법</td>
		<td>
		
		<c:if test="${param.paymentOption=='1'}">
		현금결제
		</c:if>
		<c:if test="${param.paymentOption=='2'}">
		신용결제
		</c:if>
		
		</td>
		<td></td>
	</tr>
	<tr>
		<td>구매자이름</td>
		<td>${purchase.buyer.userName}<%--<%= purchase.getBuyer().getUserName() --%></td>
		<td></td>
	</tr>
	<tr>
		<td>구매자연락처</td>
		<td>${purchase.receiverPhone}<%--<%= purchase.getBuyer().getPhone() --%></td>
		<td></td>
	</tr>
	<tr>
		<td>구매자주소</td>
		<td>${purchase.divyAddr}<%--<%= purchase.getBuyer().getAddr() --%></td>
		<td></td>
	</tr>
		<tr>
		<td>구매요청사항</td>
		<td>${purchase.divyRequest}<%--<%= purchase.getDivyRequest()--%></td>
		<td></td>
	</tr>
	<tr>
		<td>배송희망일자</td>
		<td>${purchase.divyDate}<%--<%=purchase.getDivyDate()--%></td>
		<td></td>
	</tr>
	

</table>
	<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="30"></td>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23"/>
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01"	style="padding-top: 3px;">
						<a href="/listProduct.do?menu=search">확인</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif"width="14" height="23"/>
					</td>
				</tr>
			</table>
</form>

</body>
</html>