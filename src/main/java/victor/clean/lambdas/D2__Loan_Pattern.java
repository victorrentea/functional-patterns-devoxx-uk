package victor.clean.lambdas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

// export all orders to a file


interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
abstract class BaseExporter {
	private final static Logger log = LoggerFactory.getLogger(BaseExporter.class);
			
	public File exportFile(String fileName) throws Exception {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			writeContent(writer);
			return file;
		} catch (Exception e) {
			// TODO send email notification
			log.debug("Gotcha!", e); // TERROR-Driven Programming
			throw e;
		}
	}
	protected abstract void writeContent(Writer writer) throws IOException;
}


class OrderExporter extends BaseExporter {
	private OrderRepo repo;
	protected void writeContent(Writer writer) throws IOException {
		writer.write("OrderID;Date\n");
		repo.findByActiveTrue()
		.map(o -> o.getId() + ";" + o.getCreationDate())
		.forEach(Unchecked.consumer(writer::write));
	}
	
}
class UserExporter extends BaseExporter {
//	private UserRepo repo;
	protected void writeContent(Writer writer) throws IOException {
		writer.write("UserID;FirstName...\n");
//		repo.findByActiveTrue()
//			.map(o -> o.getId() + ";" + o.getCreationDate())
//			.forEach(Unchecked.consumer(writer::write));
		
	}
}

