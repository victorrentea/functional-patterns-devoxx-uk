package victor.clean.lambdas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

// export all orders to a file

interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
class CsvExporter {
	private final static Logger log = LoggerFactory.getLogger(CsvExporter.class);


	public File export(String fileName, Consumer<Writer> contentWriter) throws IOException {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			contentWriter.accept(writer);
			return file;
		} catch (Exception e) {
			// TODO send email notification
			log.debug("Gotcha!", e); // TERROR-Driven Development
			throw e;
		}
	}

	public static void main(String[] args) throws IOException {
		CsvExporter exporter = new CsvExporter();
		OrderContentWriter orderContentWriter = new OrderContentWriter();
		UserContentWriter userContentWriter = new UserContentWriter();


		exporter.export("orders.csv", orderContentWriter::write);
		exporter.export("orders.csv", userContentWriter::write);
	}

}
class OrderContentWriter {
	private OrderRepo repo;

	@SneakyThrows
	public void write(Writer writer) {
		writer.write("OrderID;Date\n");
		repo.findByActiveTrue()
			.map(o -> o.getId() + ";" + o.getCreationDate())
			.forEach(Unchecked.consumer(writer::write));
	}
}

@Slf4j
class UserContentWriter {

	private UserRepo userRepo;

	@SneakyThrows
	public void write(Writer writer) {
		writer.write("username;ln\n");
		userRepo.findAll().stream()
				.map(u->u.getUsername() + ";" + u.getLastName())
				.forEach(Unchecked.consumer(writer::write));
	}
}

