//get/users/me/results
// 내가 진행한 감성 테스트 결과 목록 , 결과 카드 클릭시 화면 이동이 있음
import { motion } from "motion/react";
import type { MyResultItem } from "../types";

type MyResultsScreenProps = {
  myResults: MyResultItem[];
  isLoadingMyResults: boolean;
  myResultsError: string;
  loadResultDetail: (resultId: number) => void | Promise<void>;
};

export function MyResultsScreen({
  myResults,
  isLoadingMyResults,
  myResultsError,
  loadResultDetail,
}: MyResultsScreenProps) {
  return (
    <div className="space-y-8">
      <div className="text-center space-y-2">
        <h2 className="text-4xl font-medium text-gray-800">내 감성 결과</h2>
        <p className="text-gray-600">
          지금까지 저장된 감성 테스트 결과를 확인해보세요.
        </p>
      </div>

      {isLoadingMyResults && (
        <p className="text-center text-gray-700 font-medium">
          결과 목록을 불러오는 중이에요...
        </p>
      )}

      {myResultsError && (
        <p className="text-center text-red-500 font-medium">
          {myResultsError}
        </p>
      )}

      {!isLoadingMyResults && myResults.length === 0 && !myResultsError && (
        <p className="text-center text-gray-700 font-medium">
          아직 저장된 결과가 없어요.
        </p>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {myResults.map((result) => (
          <motion.button
            key={result.resultId}
            onClick={() => loadResultDetail(result.resultId)}
            className="p-6 text-left"
            style={{
              background: "#FFFFFF",
              borderRadius: "25px",
              border: "3px solid #2D2D2D",
              boxShadow: "4px 4px 0px #2D2D2D",
            }}
            whileHover={{
              y: -4,
              boxShadow: "6px 6px 0px #2D2D2D",
            }}
            whileTap={{
              y: 1,
              boxShadow: "2px 2px 0px #2D2D2D",
            }}
          >
            <div className="space-y-3">
              <div className="flex items-center justify-between gap-4">
                <h3 className="text-xl font-medium text-gray-800">
                  {result.endingName}
                </h3>
                <span className="text-sm text-gray-500">
                  #{result.resultId}
                </span>
              </div>

              <div className="space-y-1 text-gray-700">
                <p>날씨: {result.weatherName}</p>
                <p>루트: {result.routeName}</p>
                <p>대표 감정: {result.mainEmotion}</p>
                <p>총점: {result.totalScore}</p>
              </div>

              <p className="text-sm text-gray-500">
                {result.createdAt.replace("T", " ")}
              </p>

              <p className="text-sm font-medium" style={{ color: "#FF6B6B" }}>
                자세히 보기 →
              </p>
            </div>
          </motion.button>
        ))}
      </div>
    </div>
  );
}