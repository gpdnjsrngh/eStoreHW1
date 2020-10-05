package kr.ac.hansung.cse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Cart implements Serializable {
	
	//클래스의 버전 지정 이유: 서버측과 클라측의 컴퓨터가 다를 수 있어서
	private static final long serialVersionUID = -7383420736137539222L;

	//generationType디폴트는 table인데 identity가 성능면에서 더 효율적
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//fetchType디폴트는 lazy인데 그럼 코드가 달라짐
	//eager은 Cart를 부르면 cartItems도 같이 불러짐(양이 적을 때 사용, 구현 쉬움)
	//mappedBy에 의해서 테이블에 만들어지지 않음
	@OneToMany(mappedBy="cart", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<CartItem> cartItems = new ArrayList<CartItem>();
	
	private double grandTotal;
}
