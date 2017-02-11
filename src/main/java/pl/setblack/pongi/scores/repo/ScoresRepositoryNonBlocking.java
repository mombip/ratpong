package pl.setblack.pongi.scores.repo;

import javaslang.collection.List;
import javaslang.control.Option;
import pl.setblack.pongi.scores.ScoreRecord;
import pl.setblack.pongi.scores.UserScore;
import pl.setblack.pongi.users.api.RegUserStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by jarek on 2/5/17.
 */
public class ScoresRepositoryNonBlocking {
	ScoresRepository scoresRepository;

	 private final Executor writesExecutor = Executors.newSingleThreadExecutor();
	
	
    public ScoresRepositoryNonBlocking(ScoresRepository scoresRepository) {
		this.scoresRepository = scoresRepository;
	}

	public void registerScore(List<ScoreRecord> rec){
    	writesExecutor.execute( ()-> this.scoresRepository.registerScore(rec));
    }

    public CompletionStage<Option<UserScore>> getUserScore(String userId) {
    	 return CompletableFuture.completedFuture(this.scoresRepository.getUserScore(userId) );
    }

    public CompletionStage<List<UserScore>> getTopScores(final int limit) {
    	return CompletableFuture.completedFuture(this.scoresRepository.getTopScores(limit) );
    }
}
