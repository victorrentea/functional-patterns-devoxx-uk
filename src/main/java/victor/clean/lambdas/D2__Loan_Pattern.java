//package victor.clean.lambdas;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Writer;
//import java.util.function.Consumer;
//import java.util.stream.Stream;
//
//import org.jooq.lambda.Unchecked;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//// export all orders to a file
//
//interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
//	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
//}
//class OrderExporter {
//	private final static Logger log = LoggerFactory.getLogger(OrderExporter.class);
//
//	private OrderRepo repo;
//
//	public File exportFile(String fileName) {
//		File file = new File("export/" + fileName);
//		try (Writer writer = new FileWriter(file)) {
//			writer.write("OrderID;Date\n");
//			repo.findByActiveTrue()
//				.map(o -> o.getId() + ";" + o.getCreationDate())
//				.forEach(writer::write);
//			return file;
//		} catch (Exception e) {
//			// TODO send email notification
//			log.debug("Gotcha!", e); // TERROR-Driven Development
//			throw e;
//		}
//	}
//}
//
