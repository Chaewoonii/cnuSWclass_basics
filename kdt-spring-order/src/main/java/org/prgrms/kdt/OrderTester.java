package org.prgrms.kdt;

import org.apache.logging.log4j.message.Message;
import org.prgrms.kdt.configuration.AppConfigurateion;
import org.prgrms.kdt.order.OrderItem;
import org.prgrms.kdt.order.OrderProperties;
import org.prgrms.kdt.order.OrderService;
import org.prgrms.kdt.voucher.FixedAmountVoucher;
import org.prgrms.kdt.voucher.JDBCVoucherRepository;
import org.prgrms.kdt.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderTester {

    /*
     - 로거 생성. getLogger()에 이름 전달해야함. 보통 패키지명 포함해 전달.
     - getLogger(this.getClass())나 getLogger(클래스.class)로 많이 사용
     - 매 인스턴스마다 로거를 생성하는 것이 아니라 해당 클래스의 로거는 단 하나(static final)
     - 패키지마다 로그 레벨을 설정: 설정파일에서 설정.

      */
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) throws IOException{


        /*//annotaionContext로부터 Bean을 생성해 orderService를 만듦.
        var applicationContext = new AnnotationConfigApplicationContext(AppConfigurateion.class);

        var customerId = UUID.randomUUID();
        var orderService = applicationContext.getBean(OrderService.class);
//        var orderContext = new AppConfigurateion();
//        var orderService = orderContext.orderService();
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }});
        Assert.isTrue(order.totalAmount() == 100L, MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));
        */

//        var applicationContext = new AnnotationConfigApplicationContext(AppConfigurateion.class);
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AppConfigurateion.class);

        //application.properties에 작성한 property를 가져옴
        //AppConfiguration에 @PropertySource를 알려주었기 때문에 AppConfiguration을 통해 가져옴
        var environment = applicationContext.getEnvironment();

        //profile: 특징이나 공통점을 찾아 그룹화
        //예: 30대 남성
        environment.setActiveProfiles("local"); // JDBCVoucherRepository 의 Profile 로 지정한 "dev"라는 프로파일을 가져옴
        applicationContext.refresh(); //"dev"라는 프로파일을 제대로 적용하도록 refresh

        /*
        var version = environment.getProperty("kdt.version");
        var minOrderAmount = environment.getProperty("kdt.minimum-order-amount", Integer.class); // int
        var supportVendors = environment.getProperty("kdt.support-vendors", List.class); // list of String
        // getProperty에 두 번째 인자로 클래스를 지정 : 해당 클래스로 변수를 가져옴. 지정하지 않을 경우 String클래스가 됨.
        var description = environment.getProperty("kdt.description", List.class);

        System.out.println(MessageFormat.format("version ->{0}", version)); //v1.0.0
        System.out.println(MessageFormat.format("minOrderAmount ->{0}", minOrderAmount)); //1
        System.out.println(MessageFormat.format("supportVendors ->{0}", supportVendors)); //[a, b, c, e, f, g]
        System.out.println(MessageFormat.format("description ->{0}", description)); //

        //yaml과 ConfigurationProperties를 사용, properties를 하나의 클래스로 정의, 필요 시 주입받아 사용하고자 하는 경우
        var orderProperties =  applicationContext.getBean(OrderProperties.class);
        System.out.println(MessageFormat.format("version ->{0}", orderProperties.getVersion())); //v1.0.0
        System.out.println(MessageFormat.format("minOrderAmount ->{0}", orderProperties.getMinimumOrderAmount())); //1
        System.out.println(MessageFormat.format("supportVendors ->{0}", orderProperties.getSupportVendors())); //[a, b, c, e, f, g]
        System.out.println(MessageFormat.format("description ->{0}", orderProperties.getDescription())); // line 1 hello world! ...
        */

        //로그 출력.
        var orderProperties = applicationContext.getBean(OrderProperties.class);
        logger.info("logger name -> {}", logger.getName()); //org.prgrms.kdt.OrderTester 패키지이름+클래스명까지.
        logger.info("version -> {}", orderProperties.getVersion());
        logger.info("minOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
        logger.info("supportVendors -> {}", orderProperties.getSupportVendors());
        logger.info("description -> {}", orderProperties.getDescription());

        /*

        //리소스 가져오기
        //classpath에서 가져오라고 지정. classpath를 지정하지 않아도 default로 classpath에서 가져옴
        var resource = applicationContext.getResource("classpath:application.yaml");
        System.out.println(MessageFormat.format("Resource -> {0}", resource.getClass().getCanonicalName())); //어떤 구현체를 가져오는지 확인
        //org.springframework.core.io.DefaultResourceLoader.ClassPathContextResource
        var file = resource.getFile(); //가져온 resource의 내용을 읽음
        var strings = Files.readAllLines(file.toPath()); // 한줄씩 잘라서 list로 가져옴
        System.out.println(strings.stream().reduce("", (a, b) -> a + "\n" + b)); //리스트를 붙여서 문자열로.

        //working directory를 기준으로 찾음. 최상위의 kdt-spring-order 파일 아래. test파일 아래 sample.txt가져옴
        var resoucrce2 = applicationContext.getResource("file:test/sample.txt");
        System.out.println(MessageFormat.format("resoucrce2 -> {0}", resoucrce2.getClass().getCanonicalName()));
        var strings2 = Files.readAllLines(resoucrce2.getFile().toPath());
        System.out.println(strings2.stream().reduce("", (a, b) -> a + "\n" + b));

        //인터넷 url로 가져올수도 있음
        var resource3 = applicationContext.getResource("https://stackoverflow.com/");
        //url이기때문에 getURL. stream을 통해 다운받을 수 있게 해야함. >>> Channel 사용
        var readableByteChannel = Channels.newChannel(resource3.getURL().openStream());
        var bufferReader = new BufferedReader(Channels.newReader(readableByteChannel, StandardCharsets.UTF_8));
        var contents = bufferReader.lines().collect(Collectors.joining("\n")); //개행 문자 삽입
        System.out.println(contents);
        */

        //고객 생성
        var customerId = UUID.randomUUID();

        /*
        - Qualifier가 설정되었기 때문에 repository 인스턴스를 생성할때도 지정해줘야 한다. getBean도 어떤 bean을 갖고와야할지 모르기 때문
        - 각 bean의 이름을 다르게 하거나, BeanFactoryAnnotationUtils을 이용해서 가져온다.
        - 실제로 BeanFactoryAnnotationUtils를 사용해 qualifier를 지정하고 가져오는 경우는 거의 없다.
        - 대체로 Qualifier를 쓰는 경우는 여러개의 서버에 동일한 repository를 할당해야되는데 동일하기 때문에 충돌이 일어나는 경우이다.
        - 보통은 primary키워드를 주며, 사용하는 쪽에서 고민하지 않게 하는 쪽이 좋다.
         >> 아래의 코드를 변경
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);

                                                                      applicationContext가 아닌 BeanFactory를 요구                            qualifier 지정  */
//        var voucherRepository = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class, "memory");
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
        /*
          싱글톤 패턴
           : 생성자가 여러 차례 호출되더라도 최초의 생성자가 생성한 객체를 리턴. 실제로 생성되는 객체는 단 하나.
           공통된 객체를 여러개 생성해서 사용하는 DBCP 상황에서 많이 사용.
           (DBCP; DatatBase Connection Pool)

         - Xml이나 @Bean을 통해 Bean defintion을 설정한다
         - 이 Bean defintion으로부터 Bean이 한개가 만들어질 수도, 여러 개가 만들어질 수도 있다.
         - 단 하나만 만들어지는 것을 싱글톤이라고 하고, 스프링 빈은 기본적으로 싱글톤 스콥이다.
         - Spring Bean은 6개의 Bean scope가 있다.

        var voucherRepository2 = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), VoucherRepository.class, "memory");
        System.out.println(MessageFormat.format("voucherRepository{0}", voucherRepository));    //주소값
        System.out.println(MessageFormat.format("voucherRepository2{0}", voucherRepository2));  //voucherRepository와 같은 주소값
        System.out.println(MessageFormat.format("voucherRepository == voucherRepository2 => {0}", voucherRepository == voucherRepository2));    //true

        - Bean을(객체를) 여러 개 만들고 싶을 경우 해당 클래스(인터페이스)에 @Scope(value="prototype" 또는 ConfigurableBeanFactory.SCOPE_SINGLETON) 을 지정해준다.
        - @Scope을 prototype로 지정한 이후에는 voucherRepository와 voucherRepository2의 주소값이 다르게 나오고, == 연산의 결과도 false이다.
        - voucherRepository를 두개 만들 경우, voucherService가 voucherRepository를 찾을 수 없는 문제가 발생한다.(OrderService.createOrder -> VoucherService.getVoucher)
        - FixedAmountVoucher에서 voucherID를 만들어줌 -> ??
        */

        //바우처 생성
        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        //environment.setActiveProfiles("dev");로 JDBCVoucherRepository를 잘 가져왔는지 확인
        System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository instanceof JDBCVoucherRepository));
        System.out.println(MessageFormat.format("is JDBC Repo -> {0}", voucherRepository.getClass().getCanonicalName()));

        //오더서비스 객체 생성
        var orderService = applicationContext.getBean(OrderService.class);
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L, 1));
        }}, voucher.getVoucherId());
        Assert.isTrue(order.totalAmount() == 90L, MessageFormat.format("totalAmount {0} is not 100L", order.totalAmount()));

        /*Life Cycle
        - close()메서드를 호출하면 컨테이너에 등록된 모든 빈이 소멸

        Bean의 생성과 소멸에 대한 콜백
         생성
            1) postConstruct: @PostConstruct
            2) afterPorpertiesSet: InitializingBean, Override
         소멸
            3) preDestroy: @PreDestroy
            4) destroy: DisposableBean, Override
         */
        applicationContext.close();
    }
}
