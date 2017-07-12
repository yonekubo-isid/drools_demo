package drools_demo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import drools_demo.Bar;
import drools_demo.Baz;
import drools_demo.PolicyViolation;
import drools_demo.SocialExpense;
import drools_demo.Transportation;
import drools_demo.TransportationExpense;
import drools_demo.TravelExpense;

public class DemoTest {

	private KieServices keyService;
	private KieContainer kieContainer;
	
	@Before
	public void before() {
		//KieServices と KieContainer を準備する
		//クラスパス配下のDRLが読み込まれる
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
	 * expense-policy-drl の Rule-1 に違反
	 */
	@Test
	public void testTravelDistanceError() {
		//セッションの開始とエラー通知を受け取るチャネルの登録
		KieSession session = kieContainer.newKieSession();
		MockChannel<PolicyViolation> channel = new MockChannel<>();
		session.registerChannel("policy-violation", channel);
		
		//Factとなるドメインオブジェクトをセッションへ登録
		TravelExpense travel = new TravelExpense();
		travel.setDistance(99);
		travel.setTransportation(Transportation.BulletTrain);
		
		session.insert(travel);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(2)); //規程チェック1件と通知1件		
		assertThat(channel.getReceived().size(), is(1));
		assertThat(channel.getReceived().get(0).getErrorMessage(), is("distance"));
	}

	/*
	 * expense-policy-drl の Rule-1 をクリア
	 */
	@Test
	public void testTravelDistanceNormal() {
		//セッションの開始とエラー通知を受け取るチャネルの登録
		KieSession session = kieContainer.newKieSession();
		MockChannel<PolicyViolation> channel = new MockChannel<>();
		session.registerChannel("policy-violation", channel);
		
		//Factとなるドメインオブジェクトをセッションへ登録
		TravelExpense travel = new TravelExpense();
		travel.setDistance(100);
		travel.setTransportation(Transportation.BulletTrain);
		
		session.insert(travel);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(0)); //マッチ0件		
		assertThat(channel.getReceived().size(), is(0));
	}
	
	/*
	 * expense-policy-drl の Rule-2 に違反
	 */
	@Test
	public void testTransportationError() {
		//セッションの開始とエラー通知を受け取るチャネルの登録
		KieSession session = kieContainer.newKieSession();
		MockChannel<PolicyViolation> channel = new MockChannel<>();
		session.registerChannel("policy-violation", channel);
		
		//Factとなるドメインオブジェクトをセッションへ登録
		TransportationExpense trans = new TransportationExpense();
		trans.setDistance(50);
		trans.setTransportation(Transportation.LimitedExpress);
		
		session.insert(trans);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(2)); //規程チェック1件と通知1件		
		assertThat(channel.getReceived().size(), is(1));
		assertThat(channel.getReceived().get(0).getErrorMessage(), is("transportation"));
	}

	/*
	 * expense-policy-drl の Rule-1 に違反
	 */
	@Test
	public void testTransportationError2() {
		//セッションの開始とエラー通知を受け取るチャネルの登録
		KieSession session = kieContainer.newKieSession();
		MockChannel<PolicyViolation> channel = new MockChannel<>();
		session.registerChannel("policy-violation", channel);
		
		//Factとなるドメインオブジェクトをセッションへ登録
		TransportationExpense trans = new TransportationExpense();
		trans.setDistance(50);
		trans.setTransportation(Transportation.BulletTrain);
		
		session.insert(trans);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(2)); //規程チェック1件と通知1件		
		assertThat(channel.getReceived().size(), is(1));
		assertThat(channel.getReceived().get(0).getErrorMessage(), is("transportation"));
	}
	
	/*
	 * expense-policy-drl の Rule-3 に違反
	 */
	@Test
	public void testSocialError() {
		//セッションの開始とエラー通知を受け取るチャネルの登録
		KieSession session = kieContainer.newKieSession();
		MockChannel<PolicyViolation> channel = new MockChannel<>();
		session.registerChannel("policy-violation", channel);
		
		//Factとなるドメインオブジェクトをセッションへ登録
		SocialExpense social = new SocialExpense();
		social.setNumberOfPeople(3);
		social.setTotalAmount(15010);
		social.setUsage("会議費");
		
		session.insert(social);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(2)); //規程チェック1件と通知1件		
		assertThat(channel.getReceived().size(), is(1));
		assertThat(channel.getReceived().get(0).getErrorMessage(), is("conference"));
	}
	
	/*
	 * expense-policy-drl の Rule-3 をクリア
	 */
	@Test
	public void testSocialNormal() {
		//セッションの開始とエラー通知を受け取るチャネルの登録
		KieSession session = kieContainer.newKieSession();
		MockChannel<PolicyViolation> channel = new MockChannel<>();
		session.registerChannel("policy-violation", channel);
		
		//Factとなるドメインオブジェクトをセッションへ登録
		SocialExpense social = new SocialExpense();
		social.setNumberOfPeople(3);
		social.setTotalAmount(15010);
		social.setUsage("交際費");
		
		session.insert(social);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(0)); //規程チェック1件		
		assertThat(channel.getReceived().size(), is(0));
	}
	
	/*
	 * foo-bar-baz.drl のテスト。
	 */
	@Test
	public void testFooBarBaz() {
		KieSession session = kieContainer.newKieSession();
		
		Bar bar = new Bar(11);
		Baz baz = new Baz(12);
		
		session.insert(bar);
		session.insert(baz);
		
		//ルールを発火
		int fired = session.fireAllRules();

		assertThat(fired, is(2));
		assertThat(bar.isFired(), is(true));
		assertThat(baz.isFired(), is(true));
	}
	
	class MockChannel<T> implements Channel {

		private List<T> received = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		public void send(Object object) {
			received.add((T)object);
		}

		public List<T> getReceived() {
			return received;
		}
	}
	
}
