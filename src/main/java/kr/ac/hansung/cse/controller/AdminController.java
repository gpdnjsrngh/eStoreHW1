package kr.ac.hansung.cse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@RequestMapping // (클래스 레벨 request mapping과 매칭)
	public String adminPage() {
		return "admin";
	}

	@RequestMapping("/productInventory")
	public String getProducts(Model model) { // product정보를 Model에 넣어줌, model->view
		List<Product> products = productService.getProducts();
		model.addAttribute("products", products);

		return "productInventory";
	}

	// method명시안하면 GET, POST 모두 처리
	@RequestMapping(value = "/productInventory/addProduct", method = RequestMethod.GET)
	public String addProduct(Model model) {
		Product product = new Product();
		product.setCategory("컴퓨터");
		model.addAttribute("product", product);

		return "addProduct";
	}

	// 폼 데이터 자동 바인딩(via springform)
	// jsp의 sf:input path와 객체 멤버 변수 이름이 같아야함.
	@RequestMapping(value = "/productInventory/addProduct", method = RequestMethod.POST)
	public String addProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) {

		// 에러가 있으면 에러메시지 콘솔에 출력 후 다시 입력할 수 있게 addProduct.jsp페이지 반환
		if (result.hasErrors()) {
			System.out.println("Form data has some errors");
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors)
				System.out.println(error.getDefaultMessage());

			return "addProduct";
		}

		MultipartFile productImage = product.getProductImage();
		//웹 배포될 때마다 rootDirectory가 달라서 하드 코딩을 시켜줄 수가 없음
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory+"\\resources\\images\\" + productImage.getOriginalFilename());
		
		if(productImage.isEmpty() == false) {
			System.out.println("------------ file start ----------");
			System.out.println("name : " + productImage.getName());
			System.out.println("filename : " + productImage.getOriginalFilename());
			System.out.println("size : " + productImage.getSize());
			System.out.println("savePath : " + savePath);
			System.out.println("------------ file end -----------");
		}
		
		//핵심적인 부분: productImage를 파일로 저장
		if(productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		
		//db에 파일name 저장
		product.setImageFilename(productImage.getOriginalFilename());
		
		productService.addProduct(product);
		

		return "redirect:/admin/productInventory";
	}

	// {id}가 pathvariable
	@RequestMapping(value = "/productInventory/deleteProduct/{id}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable int id, HttpServletRequest request) {

		Product product = productService.getProductById(id);
		
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		//windows라서 \\, linux는 다름
		Path savePath = Paths.get(rootDirectory+"\\resources\\images\\" + product.getImageFilename());
		
		if(Files.exists(savePath)) {
			try {
				Files.delete(savePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		productService.deleteProduct(product);

		return "redirect:/admin/productInventory";

	}

	@RequestMapping(value = "/productInventory/updateProduct/{id}", method = RequestMethod.GET)
	public String updateProduct(@PathVariable int id, Model model) {
		Product product = productService.getProductById(id);

		model.addAttribute("product", product);

		return "updateProduct";

	}

	@RequestMapping(value = "/productInventory/updateProduct", method = RequestMethod.POST)
	public String updateProductPost(@Valid Product product, BindingResult result, HttpServletRequest request) {
		// 에러가 있으면 에러메시지 콘솔에 출력 후 다시 입력할 수 있게 updateProduct.jsp페이지 반환
		if (result.hasErrors()) {
			System.out.println("Form data has some errors");
			List<ObjectError> errors = result.getAllErrors();

			for (ObjectError error : errors)
				System.out.println(error.getDefaultMessage());

			return "updateProduct";
		}
		
		MultipartFile productImage = product.getProductImage();
		//웹 배포될 때마다 rootDirectory가 달라서 하드 코딩을 시켜줄 수가 없음
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		Path savePath = Paths.get(rootDirectory+"\\resources\\images\\" + productImage.getOriginalFilename());
		
		if(productImage.isEmpty() == false) {
			System.out.println("------------ file start ----------");
			System.out.println("name : " + productImage.getName());
			System.out.println("filename : " + productImage.getOriginalFilename());
			System.out.println("size : " + productImage.getSize());
			System.out.println("savePath : " + savePath);
			System.out.println("------------ file end -----------");
		}
		
		//핵심적인 부분: productImage를 파일로 저장
		if(productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(savePath.toString()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		
		//db에 파일name 저장
		product.setImageFilename(productImage.getOriginalFilename());
		
		productService.updateProduct(product);

		return "redirect:/admin/productInventory";
	}
}
