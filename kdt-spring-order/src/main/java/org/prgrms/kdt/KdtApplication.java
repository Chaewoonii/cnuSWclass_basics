package org.prgrms.kdt;

import org.prgrms.kdt.order.OrderProperties;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.JDBCVoucherRepository;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.MessageFormat;
import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgrms.kdt.order","org.prgrms.kdt.voucher", "org.prgrms.kdt.configuration"}) //AppConfiguration 에서 가져옴
public class KdtApplication {

	public static void main(String[] args)
	{
		//SpringApplication.run(KdtApplication.class, args);
		// - 위의 코드를 아래와 같이 변경하여 profile 지정
		var springApplication = new SpringApplication(KdtApplication.class);
		springApplication.setAdditionalProfiles("local");
		var applicationContext = springApplication.run(args);
		/*
		 - 또는 오른쪽 상단의 KdtApplication - Edit Configurations... Active profiles에 전달할 수 있음
		 - 또는 KdtApplication - Edit Configurations... - Program arguments에 --spring.profiles.active=local 로 지정할수도 있음

		 - 여러 개의 파일로 나눌 경우: spring.config.activate.on-profile 은 필요 없음
		 	application.yaml -> default로 적용됨
		 	application-local.yaml
		 	application-dev.yaml

		 - Repository 에 @Profile({"local", "default"})와 같이 여러 개로 지정할 수 있음
		 - 또는 KdtApplication - Edit Configurations... - Program arguments에 --spring.profiles.active=dev면
		 MemoryVoucherRepository 에 @Profile({"local", "default"})를 붙여 Memory가 기본이어도,
		 Program arguments에 --spring.profiles.active=dev 로 설정하면 profile 이 dev 인 것을 가져옴
		 */

		var orderProperties =  applicationContext.getBean(OrderProperties.class);
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
		System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository.getClass().getCanonicalName()));

	}

}
