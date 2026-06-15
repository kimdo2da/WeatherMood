// /results/resultId
//선택한 결과 상세 표시, 날씨/루트/대표감정/엔딩 설명 표시, 감성 처방전 표시
// 반전 추천 세트추천 일기작성 버튼이 있음
import { motion } from "motion/react";
import { CozyCard } from "../components/CozyCard";
import type { RecommendationItem, ResultDetailData } from "../types";
import { convertRecommendationsToCards } from "../converters";

type ResultDetailScreenProps = {
  resultDetail: ResultDetailData | null;
  isLoadingResultDetail: boolean;
  resultDetailError: string;
  loadMyResults: () => void | Promise<void>;
  openDiaryWrite: (resultId: number) => void;
};

export function ResultDetailScreen({
  resultDetail,
  isLoadingResultDetail,
  resultDetailError,
  loadMyResults,
  openDiaryWrite,
}: ResultDetailScreenProps) {
  return (
    <div className="space-y-10">
      <div className="flex justify-between items-center gap-4">
        <motion.button
          onClick={loadMyResults}
          className="px-5 py-3 font-medium"
          style={{
            background: "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow: "3px 3px 0px #2D2D2D",
          }}
          whileHover={{
            y: -2,
            boxShadow: "4px 4px 0px #2D2D2D",
          }}
          whileTap={{
            y: 1,
            boxShadow: "2px 2px 0px #2D2D2D",
          }}
        >
          ← 목록으로
        </motion.button>
      </div>

      {isLoadingResultDetail && (
        <p className="text-center text-gray-700 font-medium">
          결과 상세를 불러오는 중이에요...
        </p>
      )}

      {resultDetailError && (
        <p className="text-center text-red-500 font-medium">
          {resultDetailError}
        </p>
      )}

      {resultDetail && (
        <div className="space-y-10">
          <motion.div
            className="p-8 text-center space-y-5"
            style={{
              background: "#FFFFFF",
              borderRadius: "30px",
              border: "4px solid #2D2D2D",
              boxShadow: "6px 6px 0px #2D2D2D",
            }}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            <h2 className="text-4xl text-gray-800 font-medium">
              {resultDetail.ending.endingName}
            </h2>

            <p className="text-gray-600 text-lg max-w-2xl mx-auto">
              {resultDetail.ending.description}
            </p>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 text-gray-700">
              <div>날씨: {resultDetail.weather.weatherName}</div>
              <div>루트: {resultDetail.route.routeName}</div>
              <div>대표 감정: {resultDetail.mainEmotion}</div>
              <div>총점: {resultDetail.totalScore}</div>
            </div>

            <p className="text-sm text-gray-500">
              {resultDetail.createdAt.replace("T", " ")}
            </p>

            <motion.button
              onClick={() => openDiaryWrite(resultDetail.resultId)}
              className="px-6 py-3 font-medium"
              style={{
                background: "#FFD93D",
                color: "#2D2D2D",
                borderRadius: "25px",
                border: "3px solid #2D2D2D",
                boxShadow: "4px 4px 0px #2D2D2D",
              }}
              whileHover={{
                y: -2,
                boxShadow: "5px 5px 0px #2D2D2D",
              }}
              whileTap={{
                y: 1,
                boxShadow: "2px 2px 0px #2D2D2D",
              }}
            >
              이 결과로 일기 작성
            </motion.button>
          </motion.div>

          <div className="space-y-4">
            <h3 className="text-2xl font-medium text-gray-800">
              저장된 추천 콘텐츠
            </h3>

            <div className="space-y-10">
              <div className="space-y-4">
                <h3 className="text-2xl font-medium text-gray-800">
                  감성 처방전
                </h3>

                <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
                  {convertRecommendationsToCards(
                    resultDetail.recommendations?.prescription ?? []
                  ).map((content) => (
                    <CozyCard
                      key={`detail-prescription-${content.type}-${content.title}`}
                      {...content}
                    />
                  ))}
                </div>
              </div>

              <div className="space-y-4">
                <h3 className="text-2xl font-medium text-gray-800">
                  반전 추천
                </h3>

                <div className="grid grid-cols-2 md:grid-cols-2 gap-6">
                  {convertRecommendationsToCards(
                    resultDetail.recommendations?.reverse ?? []
                  ).map((content) => (
                    <CozyCard
                      key={`detail-reverse-${content.type}-${content.title}`}
                      {...content}
                    />
                  ))}
                </div>
              </div>

              <div className="space-y-4">
                <h3 className="text-2xl font-medium text-gray-800">
                  세트 추천
                </h3>

                <div className="grid grid-cols-2 md:grid-cols-2 gap-6">
                  {convertRecommendationsToCards(
                    [
                      resultDetail.recommendations?.set?.anime,
                      resultDetail.recommendations?.set?.drama,
                    ].filter(Boolean) as RecommendationItem[]
                  ).map((content) => (
                    <CozyCard
                      key={`detail-set-${content.type}-${content.title}`}
                      {...content}
                    />
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}