package drools_demo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import drools_demo.Person;

public class DslTest {

	private KieServices keyService;
	private KieContainer kieContainer;
	
	@Before
	public void before() {
		keyService = KieServices.Factory.get();
		kieContainer = keyService.getKieClasspathContainer();
        
        Results results = kieContainer.verify();
        
        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.printf("[%s] - %s[%s,%s]: %s", message.getLevel(), message.getPath(), message.getLine(), message.getColumn(), message.getText());
            }
            
            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }
	}
	
	/*
	 * DSLによるルール記述のテスト (person.dslr と person.dsl) 
	 */
	@Test
	public void testDsl() {
		//セッションの開始
		KieSession session = kieContainer.newKieSession();
		
		//Factとなるドメインオブジェクトをセッションへ登録
		Person john = new Person("John", "Smith", 35, "Manager");
		Person donald = new Person("Donald", "Trump", 70, "President");
		
		session.insert(john);
		session.insert(donald);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(1)); //John will be matched		
		assertThat(john.isHired(), is(true));
		assertThat(donald.isHired(), is(false));
	}
}
