package drools_demo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import drools_demo.Bar;
import drools_demo.Baz;

public class FileBaseTest {

	/*
	 * KieHelperを使用し、リソースを直接読み込んでルールモデルを形成するやり方のテスト
	 */
	@Test
	public void testLoadRulesFromFileResource() throws FileNotFoundException {
		KieHelper kieHelper = new KieHelper();
						
		kieHelper.addResource(ResourceFactory.newClassPathResource("foo-bar-baz2.drl"), ResourceType.DRL);
				
		Results results = kieHelper.verify();
		
		assertThat(results.hasMessages( Message.Level.ERROR ), is(false));
				
		KieSession session = kieHelper.build().newKieSession();
		
		assertThat(session, notNullValue());
		
		Bar bar = new Bar(21);
		Baz baz = new Baz(22);
		
		session.insert(bar);
		session.insert(baz);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(2));
		assertThat(bar.isFired(), is(true));
		assertThat(baz.isFired(), is(true));
	}
	
}
