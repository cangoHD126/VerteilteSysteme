package var.web.ws.poll;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.EncodeException;

public class BallotBox {
	private final Map<String, Integer> votes = new HashMap<>();
	private final Set<ElectionService> observers = new HashSet<>();
	private static BallotBox instance = new BallotBox();

	private BallotBox() {
		super();
	}

	public static synchronized BallotBox getInstance() {
		return BallotBox.instance;
	}

	public synchronized void addObserver(ElectionService observer) {
		this.observers.add(observer);
	}

	public synchronized void removeObserver(ElectionService observer) {
		this.observers.remove(observer);
	}

	public synchronized void vote(String choice) throws IOException {
		Integer voteCount = this.votes.get(choice);
		if (voteCount == null) {
			voteCount = 0;
		}
		this.votes.put(choice, voteCount + 1);
		for (final ElectionService observer : this.observers) {
			try {
				observer.notify(this);
			} catch (final EncodeException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized int countVotes() {
		int sum = 0;
		for (final Map.Entry<String, Integer> choice : this.votes.entrySet()) {
			sum += choice.getValue();
		}
		return sum;
	}

	public synchronized int getNumberOfVotes(String choice) {
		Integer voteCount = this.votes.get(choice);
		if (voteCount == null) {
			voteCount = 0;
		}
		return voteCount;
	}

	public synchronized Set<String> getChoices() {
		return this.votes.keySet();
	}
}