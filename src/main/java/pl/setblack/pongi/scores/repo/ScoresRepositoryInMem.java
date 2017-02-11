package pl.setblack.pongi.scores.repo;

import com.google.common.collect.Lists;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.PriorityQueue;
import javaslang.control.Option;
import pl.setblack.pongi.scores.ScoreRecord;
import pl.setblack.pongi.scores.UserScore;
import pl.setblack.pongi.users.repo.UserData;

import java.util.*;
import java.util.stream.Collectors;

public class ScoresRepositoryInMem implements ScoresRepository{

    private volatile HashMap<String, UserScore> allScores = HashMap.empty();

    public ScoresRepositoryInMem() {

    }

    @Override
    public void registerScore(List<ScoreRecord> rec) {
        for(ScoreRecord score : rec) {
            UserScore userScore = allScores.getOrElse(score.userId, UserScore.emptyFor(score.userId)).add(score);
            allScores = allScores.put(score.userId, userScore);
        }
    }

    @Override
    public Option<UserScore> getUserScore(String userId) {
        return allScores.get(userId);
    }

    @Override
    public List<UserScore> getTopScores(int limit) {
        Map<String, UserScore> result = new LinkedHashMap<>();
        return allScores.toJavaMap().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(o -> o.totalScore)))
                .map(e -> e.getValue())
                .collect(Collectors.toList());

        return result.entrySet()
                .stream()
                .limit(limit));
    }


}
