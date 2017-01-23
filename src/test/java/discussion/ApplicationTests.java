package discussion;

import discussion.models.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTests {

	final String BASE_URL = "http://localhost:8080/api/";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void addNewTest() {
		Entry entry = new Entry(){{
			setText("New Entry test");
			setTitle("New Entry test");
		}};
		ResponseEntity<Entry> responseEntity = restTemplate.postForEntity(String.format("%s/entry/new", BASE_URL), entry, Entry.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String URI = responseEntity.getHeaders().getLocation().toString().replace(BASE_URL, "");
        assertThat(URI.matches("entry/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")).isEqualTo(true);
	}

	@Test
    public void getEntryTest(){
        Entry entry = new Entry(){{
            setText("New Entry test");
            setTitle("New Entry test");
        }};
        ResponseEntity<Entry> responseEntity = restTemplate.postForEntity(String.format("%s/entry/new", BASE_URL), entry, Entry.class);

        ResponseEntity<Entry> getEntity = restTemplate.getForEntity(responseEntity.getHeaders().getLocation(), Entry.class);
        assertThat(getEntity.getBody().getText()).isEqualTo("New Entry test");
        assertThat(getEntity.getBody().getTitle()).isEqualTo("New Entry test");
    }

}
