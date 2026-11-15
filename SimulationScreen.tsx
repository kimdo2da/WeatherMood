//GET /simulation/questions
//POST /simulation/submit
// 질문 출력, 선택지 클릭, choiceId저장, 결과 화면 출력, 처방전/반전추천/세트추천 카드 출력
import { motion } from "motion/react";
import { CozyCard } from "../components/CozyCard";
import { SimulationChoice } from "../components/SimulationChoice";
import type {
  RecommendationItem,
  SimulationQuestionView,
  SimulationSubmitResponseData,
} from "../types";
import { convertRecommendationsToCards } from "../converters";

type SimulationScreenProps = {
  simulationQuestions: SimulationQuestionView[];
  simulationStep: number;
  setSimulationStep: React.Dispatch<React.SetStateAction<number>>;
  setSelectedChoiceIds: React.Dispatch<React.SetStateAction<number[]>>;
  handleSubmitSimulation: (choiceIds: number[]) => void | Promise<void>;

  simulationResult: SimulationSubmitResponseData | null;
  isSubmittingSimulation: boolean;
  simulationError: string;

  resetSimulation: () => void;
};

export function SimulationScreen({
  simulationQuestions,
  simulationStep,
  setSimulationStep,
  setSelectedChoiceIds,
  handleSubmitSimulation,
  simulationResult,
  isSubmittingSimulation,
  simulationError,
  resetSimulation,
}: SimulationScreenProps) {
  return (
    <div className="min-h-[60vh] flex items-center justify-center">
      {simulationQuestions.length === 0 ? (
        <p className="text-gray-700 text-lg font-medium">
          감성 테스트 질문을 불러오는 중이에요...
        </p>
      ) : simulationStep < simulationQuestions.length ? (
        <SimulationChoice
          {...simulationQuestions[simulationStep]}
          onSelect={(option) => {
            const choiceId = Number(option.id);

            setSelectedChoiceIds((prev) => {
              const next = [...prev, choiceId];
              console.log("선택한 choiceIds:", next);

              if (simulationStep + 1 >= simulationQuestions.length) {
                handleSubmitSimulation(next);
              }

              return next;
            });

            setSimulationStep((prev) => prev + 1);
          }}
        />
      ) : (
        <motion.div
          className="text-center space-y-8"
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
        >
          <motion.div
            animate={{
              rotate: [0, 10, -10, 0],
              scale: [1, 1.1, 1],
            }}
            transition={{ duration: 3, repeat: Infinity }}
          >
            <svg
              className="w-24 h-24 mx-auto"
              viewBox="0 0 100 100"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <circle
                cx="50"
                cy="50"
                r="8"
                fill="#FFD93D"
                stroke="#2D2D2D"
                strokeWidth="2"
              />
              <g>
                <line
                  x1="50"
                  y1="20"
                  x2="50"
                  y2="35"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="50"
                  y1="65"
                  x2="50"
                  y2="80"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="20"
                  y1="50"
                  x2="35"
                  y2="50"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="65"
                  y1="50"
                  x2="80"
                  y2="50"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="28"
                  y1="28"
                  x2="38"
                  y2="38"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="62"
                  y1="62"
                  x2="72"
                  y2="72"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="72"
                  y1="28"
                  x2="62"
                  y2="38"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
                <line
                  x1="38"
                  y1="62"
                  x2="28"
                  y2="72"
                  stroke="#2D2D2D"
                  strokeWidth="3"
                  strokeLinecap="round"
                />
              </g>
            </svg>
          </motion.div>

          <h2 className="text-4xl text-gray-800 font-medium">
            {simulationResult
              ? simulationResult.ending.endingName
              : isSubmittingSimulation
                ? "결과를 생성하고 있어요..."
                : "결과 준비 중"}
          </h2>

          <p className="text-gray-600 text-lg max-w-md mx-auto">
            {simulationResult
              ? simulationResult.ending.description
              : "선택한 답변과 현재 날씨를 바탕으로 감성 결과를 분석하고 있어요."}
          </p>

          {simulationResult && (
            <div className="space-y-1 text-gray-700">
              <p>루트: {simulationResult.route.routeName}</p>
              <p>대표 감정: {simulationResult.mainEmotion}</p>
              <p>총점: {simulationResult.route.totalScore}</p>
            </div>
          )}

          {simulationError && (
            <p className="text-red-500 font-medium">{simulationError}</p>
          )}

          {simulationResult && (
            <div className="space-y-10 mt-8">
              <div className="space-y-4">
                <h3 className="text-2xl font-medium text-gray-800">
                  감성 처방전
                </h3>
                <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
                  {convertRecommendationsToCards(
                    simulationResult.recommendations.prescription
                  ).map((content) => (
                    <CozyCard
                      key={`prescription-result-${content.type}-${content.title}`}
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
                    simulationResult.recommendations.reverse
                  ).map((content) => (
                    <CozyCard
                      key={`reverse-result-${content.type}-${content.title}`}
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
                      simulationResult.recommendations.set.anime,
                      simulationResult.recommendations.set.drama,
                    ].filter(Boolean) as RecommendationItem[]
                  ).map((content) => (
                    <CozyCard
                      key={`set-result-${content.type}-${content.title}`}
                      {...content}
                    />
                  ))}
                </div>
              </div>
            </div>
          )}

          <motion.button
            onClick={resetSimulation}
            className="mt-6 px-8 py-4 font-medium"
            style={{
              background: "#FFD93D",
              color: "#2D2D2D",
              borderRadius: "30px",
              border: "4px solid #2D2D2D",
              boxShadow: "6px 6px 0px #2D2D2D",
            }}
            whileHover={{
              y: -4,
              boxShadow: "8px 8px 0px #2D2D2D",
            }}
            whileTap={{
              y: 2,
              boxShadow: "3px 3px 0px #2D2D2D",
            }}
          >
            처음으로 돌아가기
          </motion.button>
        </motion.div>
      )}
    </div>
  );
}