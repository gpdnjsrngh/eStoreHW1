package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="product")
public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -567117648902464025L;

	//MySQL5에서는 GenerationType 디폴트가 TABLE(성능 안좋음)
	//identity: hibernate Sequence table을 만들지 않고 auto increment해줌
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="product_id")
	private int id;
	
	@NotEmpty(message="The product name must not be null")
	private String name;
	
	private String category;
	
	@Min(value=0, message="The product price must not be less than zero")
	private int price;
	
	@NotEmpty(message="The product manufacturer must not be null")
	private String manufacturer;
	
	@Min(value=0, message="The product unitInStock must not be less than zero")
	private int unitInStock;
	
	private String description;
	
	//파일 시스템에 저장
	//MultipartFile: file name, 경로 등 파일 정보 저장
	@Transient
	private MultipartFile productImage;
	
	//DB에는 파일 경로만 저장
	private String imageFilename;
}
