package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	//setter Method 구현 않음
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addPurchaseView(@RequestParam("prodNo") int prodNo) throws Exception{
		System.out.println("/addPurchaseView.do");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", productService.getProduct(prodNo));
		return modelAndView;
	}
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String userId) throws Exception{
		System.out.println("/addPurchase.do");
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setBuyer(userService.getUser(userId));
		purchase.setTranCode("2  ");
		
		purchaseService.addPurchase(purchase);
		purchase=purchaseService.getPurchase2(prodNo);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase(@ModelAttribute("search") Search search, HttpSession session, Model model) throws Exception{
		System.out.println("/listPurchase.do");
	
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, ((User)session.getAttribute("user")).getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase(@RequestParam("tranNo") int tranNo, Model model) throws Exception{
		System.out.println("/getPurchase.do");
		model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView(@RequestParam("tranNo") int tranNo, Model model) throws Exception{
		System.out.println("/updatePurchaseView.do");
		model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("tranNo") int tranNo, Model model) throws Exception{
		System.out.println("/updatePurchase.do");
		purchase.setPurchaseProd(purchaseService.getPurchase(tranNo).getPurchaseProd());
		
		purchaseService.updatePurchase(purchase);
		
		model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
		return "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping("/deletePurchase.do")
	public String deletePurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/deletePurchase.do");

		purchaseService.deletePurchase(purchaseService.getPurchase(tranNo));
		
		return "redirect:/listPurchase.do";
	}
	
	@RequestMapping("/updateTranCode.do")
	public String updateTranCode(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/updateTranCode.do");
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode("4  ");
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/listPurchase.do";
	}
	
	@RequestMapping("/updateTranCodeByProd.do")
	public String updateTranCodeByProd(@RequestParam("prodNo") int prodNo) throws Exception{
		System.out.println("/updateTranCodeByProd.do");
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		purchase.setTranCode("3  ");
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/listProduct.do?menu=manage";
	}
}