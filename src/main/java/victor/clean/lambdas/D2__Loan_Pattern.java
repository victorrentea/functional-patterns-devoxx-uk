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

import lombok.SneakyThrows;

// export all orders to a file


interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
class FileExporter {
	private final static Logger log = LoggerFactory.getLogger(FileExporter.class);
			
	public File exportFile(String fileName, Consumer<Writer> contentWriter) throws Exception {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			contentWriter.accept(writer);
			return file;
		} catch (Exception e) {
			// TODO send email
			log.debug("Gotcha!", e); // TERROR-Driven Development
			throw e;
		}
	}
}

class Client {
	public static void main(String[] args) throws Exception {
		FileExporter fileExporter = new FileExporter();
		OrderExportWriter orderExportWriter = new OrderExportWriter();
		UserExportWriter userExportWriter = new UserExportWriter();
		
		fileExporter.exportFile("orders.csv", orderExportWriter::writeContent);
		fileExporter.exportFile("users.csv", Unchecked.consumer(userExportWriter::writeContent));
	}
}

class OrderExportWriter {
	private OrderRepo repo;
	
	@SneakyThrows
	protected void writeContent(Writer writer) {
		writer.write("OrderID;Date\n");
		repo.findByActiveTrue()
		.map(o -> o.getId() + ";" + o.getCreationDate())
		.forEach(Unchecked.consumer(writer::write));
	}
}

class UserExportWriter  {
	protected void writeContent(Writer writer) throws IOException {
		writer.write("UserId;FirstName\n");
//		repo.findByActiveTrue()
//			.map(o -> o.getId() + ";" + o.getCreationDate())
//			.forEach(Unchecked.consumer(writer::write));
	}
}


