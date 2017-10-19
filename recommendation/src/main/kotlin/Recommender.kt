import java.io.*
import java.util.*

import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood
import org.apache.mahout.cf.taste.recommender.RecommendedItem
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender
import org.apache.mahout.cf.taste.similarity.ItemSimilarity
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity
import org.apache.mahout.cf.taste.common.TasteException
import org.apache.mahout.cf.taste.impl.model.file.*
import org.apache.mahout.cf.taste.impl.neighborhood.*
import org.apache.mahout.cf.taste.impl.recommender.*
import org.apache.mahout.cf.taste.impl.similarity.*
import org.apache.mahout.cf.taste.model.*
import org.apache.mahout.cf.taste.neighborhood.*
import org.apache.mahout.cf.taste.recommender.*
import org.apache.mahout.cf.taste.similarity.*


class UserBasedRecommender {
    @Throws(TasteException::class)
    fun performRecommendation(model: DataModel, neighbour: UserNeighborhood, similarity: UserSimilarity, Type: String) {
        val recommender = GenericUserBasedRecommender(model, neighbour, similarity)

        val userId: Long = 1
        val numberOfRecommendation = 2
        val recommendations = recommender.recommend(userId, numberOfRecommendation)

        for (recommendation in recommendations) {
            println("The two recommended item using similarity " + Type + "for user " + userId + " is " + recommendation)
        }

        val userID = 1
        val itemID: Long = 1106

        println("The estimated prefrence using similarity " + Type + "for user " + userId + " is "
                + recommender.estimatePreference(userID.toLong(), itemID))
    }
}

class ItemBasedRecommender {
    @Throws(TasteException::class)
    fun performItemRecommendation(model: DataModel, itemSimilarity: ItemSimilarity, Type: String) {
        val userId: Long = 1
        val numberOfRecommendation = 2

        val itemRecommender = GenericItemBasedRecommender(model,
                itemSimilarity)

        val itemBasedRecommendations = itemRecommender
                .recommend(userId, numberOfRecommendation)

        for (recommendation in itemBasedRecommendations) {
            println("The two recommended item using similarity " + Type + "for user " + userId + " is " + recommendation)
        }

        val userID = 1
        val itemID: Long = 1106

        println("The estimated prefrence using similarity " + Type + "for user " + userId + " is "
                + itemRecommender.estimatePreference(userID.toLong(), itemID))
    }
}

fun main(args: Array<String>) {
    var trainingFile: File? = null
    var userBasedRecommendOrItem: Boolean = true
    if (args.isNotEmpty()) {
        trainingFile = File(args[0])
        if (args[1] == "I")
            userBasedRecommendOrItem = false
    }
    when (trainingFile?.exists()) {
        false, null -> {
            print("Consider giving an input file")
            System.exit(1)
        }
        else -> {}
    }

    val model: DataModel = FileDataModel(trainingFile)

    if (userBasedRecommendOrItem) {
        val pearsonSimilarity: UserSimilarity = PearsonCorrelationSimilarity(model)
        val euclideanSimilarity: UserSimilarity = EuclideanDistanceSimilarity(model)
        val tanimotoSimilarity: UserSimilarity = TanimotoCoefficientSimilarity(model)
        val logLikelihoodSimilarity: UserSimilarity = LogLikelihoodSimilarity(model)

        val pearsonNeighborhood: UserNeighborhood =
                NearestNUserNeighborhood(1000, pearsonSimilarity, model)
        val euclideanNeighborhood: UserNeighborhood =
                NearestNUserNeighborhood(1000, euclideanSimilarity, model)
        val tanimotoNeighborhood: UserNeighborhood =
                NearestNUserNeighborhood(1000, tanimotoSimilarity, model)
        val logLikilihoodNeighborhood: UserNeighborhood =
                NearestNUserNeighborhood(1000, logLikelihoodSimilarity, model)

        val pearsonThresNeighborhood: UserNeighborhood =
                ThresholdUserNeighborhood(0.1, pearsonSimilarity, model)
        val euclideanThresNeighborhood: UserNeighborhood =
                ThresholdUserNeighborhood(0.1, euclideanSimilarity, model)
        val tanimotoThresNeighborhood: UserNeighborhood =
                ThresholdUserNeighborhood(0.1, tanimotoSimilarity, model)
        val logLikelihoodThresNeighborhood: UserNeighborhood =
                ThresholdUserNeighborhood(0.1, logLikelihoodSimilarity, model)

        val recommender = UserBasedRecommender()

        recommender.performRecommendation(model, pearsonNeighborhood, pearsonSimilarity, "pearson ")
        recommender.performRecommendation(model, euclideanNeighborhood, euclideanSimilarity, "euclidean ")
        recommender.performRecommendation(model, tanimotoNeighborhood, tanimotoSimilarity, "tanimoto ")
        recommender.performRecommendation(model, logLikilihoodNeighborhood, logLikelihoodSimilarity, "log-likelihood ")

        recommender.performRecommendation(model, pearsonThresNeighborhood, pearsonSimilarity, "pearson ")
        recommender.performRecommendation(model, euclideanThresNeighborhood, euclideanSimilarity, "euclidean ")
        recommender.performRecommendation(model, tanimotoThresNeighborhood, tanimotoSimilarity, "tanimoto ")
        recommender.performRecommendation(model, logLikelihoodThresNeighborhood, logLikelihoodSimilarity, "log-likelihood ")

    } else {
        val pearsonSimilarity = PearsonCorrelationSimilarity(model)
        val euclideanSimilarity = EuclideanDistanceSimilarity(model)
        val tanimotoSimilarity = TanimotoCoefficientSimilarity(model)
        val logLikilihoodSimilarity = LogLikelihoodSimilarity(model)

        val recommender = ItemBasedRecommender()

        recommender.performItemRecommendation(model, pearsonSimilarity, "pearson ")
        recommender.performItemRecommendation(model, euclideanSimilarity, "euclidean ")
        recommender.performItemRecommendation(model, tanimotoSimilarity, "tanimoto ")
        recommender.performItemRecommendation(model, logLikilihoodSimilarity,"log-likelihood ")
    }
}