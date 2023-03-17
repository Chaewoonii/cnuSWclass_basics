package org.prgrms.kdt;

import org.prgrms.kdt.order.OrderProperties;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.JDBCVoucherRepository;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.MessageFormat;
import java.util.UUID;

/*
* @SpringBootApplication
* - @ConfigurationProperties, @EnableConfigurationProperties 등 사용하는 property를 지정하지 않아도
*   yaml 등 configuration property 를 사용할 수 있다.
* - 별다른 설정 없이 외부 리소스를 가져오고 configuration을 정의(yaml)하는 부분의 설정을 가져올 수 있다
* - AppConfiguration에 @PropertySource(value = "application.yml", factory = YamlPropertiesFactory.class)를 설정하지 않아도
*   SpringBoot는 기본적으로 yaml(properties)을 읽는다.(이름은 application.yml, application-no.properties 이어야 함.)
*
* COC(Conversion over Configuration)
*   : 설정보다 관례를 중요시한다.
* - 모든 것을 처음부터 설정하기 보다는 관례에 따라서 기본적으로 설정이 되고, 특별하게 적용해야하는 것들에만 설정을 적용하자.
* - 즉, 필요한 것만 정의하고 나머지는 best practice 를 따르면 application 구성을 잘 할 수 있다.
* - 단순성을 확보하고 유연성을 잃어버리지 않는 장점이 있음.
 * */
//@SpringBootApplication //자동으로 yaml, configuration properties, 등 사용할 수 있음
//@ComponentScan(basePackages = {"org.prgrms.kdt.order","org.prgrms.kdt.voucher"}) //AppConfiguration 에서 가져옴
public class KdtApplication {
	private static final Logger logger = LoggerFactory.getLogger(KdtApplication.class);

	public static void main(String[] args){
		//var applicationContext = SpringApplication.run(KdtApplication.class, args);
		// - 위의 코드를 아래와 같이 변경하여 profile 지정
		var springApplication = new SpringApplication(KdtApplication.class);
		springApplication.setAdditionalProfiles("local");
		var applicationContext = springApplication.run(args);
		/*
		 - 또는 오른쪽 상단의 KdtApplication - Edit Configurations... Active profiles에 전달할 수 있음
		 - 또는 KdtApplication - Edit Configurations... - Program arguments에 --spring.profiles.active=local 로 지정할수도 있음

		 - 여러 개의 파일로 나눌 경우: spring.config.activate.on-profile 은 필요 없음
		 	application.yml -> default로 적용됨
		 	application-local.yml
		 	application-dev.yaml

		 - Repository 에 @Profile({"local", "default"})와 같이 여러 개로 지정할 수 있음
		 - 또는 KdtApplication - Edit Configurations... - Program arguments에 --spring.profiles.active=dev면
		 MemoryVoucherRepository 에 @Profile({"local", "default"})를 붙여 Memory가 기본이어도,
		 Program arguments에 --spring.profiles.active=dev 로 설정하면 profile 이 dev 인 것을 가져옴
		 */

		/*var orderProperties =  applicationContext.getBean(OrderProperties.class);
		System.out.println(MessageFormat.format("version ->{0}", orderProperties.getVersion())); //v1.0.0
		System.out.println(MessageFormat.format("minOrderAmount ->{0}", orderProperties.getMinimumOrderAmount())); //1
		System.out.println(MessageFormat.format("supportVendors ->{0}", orderProperties.getSupportVendors())); //[a, b, c, e, f, g]
		System.out.println(MessageFormat.format("description ->{0}", orderProperties.getDescription())); // line 1 hello world! ...

		//고객 생성
		var customerId = UUID.randomUUID();
		var voucherRepository = applicationContext.getBean(VoucherRepository.class);

		//바우처 생성
		var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

		System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository instanceof JDBCVoucherRepository));
		System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository.getClass().getCanonicalName()));*/

		System.out.println("***************************");
		var orderProperties = applicationContext.getBean(OrderProperties.class);
		logger.debug("logger name -> {}", logger.getName()); //org.prgrms.kdt.OrderTester 패키지이름+클래스명까지.
		logger.warn("version -> {}", orderProperties.getVersion());
		logger.warn("minOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
		logger.error("supportVendors -> {}", orderProperties.getSupportVendors());
		logger.error("description -> {}", orderProperties.getDescription());
		System.out.println("***************************");
	}

}
