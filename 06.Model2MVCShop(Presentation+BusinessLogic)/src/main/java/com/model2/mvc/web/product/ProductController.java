package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> 회원관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception{
		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product) throws Exception{
		System.out.println("/addProduct.do");
		productService.addProduct(product);
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, @RequestParam(value="menu", required=false) String menu, @CookieValue(value="history", required=false) Cookie cookie, HttpServletResponse response, Model model) throws Exception{
		System.out.println("/getProduct.do");
		
		model.addAttribute("product", productService.getProduct(prodNo));
		
		//cookie 열어본 항목
		if(cookie!=null) {
			if( !(cookie.getValue().contains(new Integer(prodNo).toString())) ){
				cookie.setValue(cookie.getValue()+","+prodNo);
			}
			response.addCookie(cookie);
		}else {
			response.addCookie(new Cookie("history", new Integer(prodNo).toString()));
		}
		
		if(menu!=null && menu.equals("manage")) {
			return "forward:/updateProductView.do";
		}else {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView() throws Exception{
		System.out.println("/updateProductView.do");	
		
		return "forward:/product/updateProductView.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product) throws Exception{
		System.out.println("/updateProduct.do");
		
		productService.updateProduct(product);
		
		return "forward:/getProduct.do";
	}
	
	@RequestMapping("/deleteProduct.do")
	public String deleteProduct(@RequestParam("prodNo") int prodNo) throws Exception{
		System.out.println("/deleteProduct.do");
		
		productService.deleteProduct(prodNo);
		
		return "redirect:/listProduct.do?menu=manage";
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, Model model) throws Exception{
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/product/listProduct.jsp";
	}
}