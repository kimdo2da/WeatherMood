//GET /statistics/recent
//GET /statistics/me
// 전체 통계 표시, 내 통계 표시, 감정별 통계 표시 최근 결과목록표시가 있음

import { motion } from "motion/react";
import { StatsDisplay } from "../components/StatsDisplay";
import type { MyStatisticsData, StatsData, StatsMode } from "../types";

type StatsScreenProps = {
  statsData: StatsData;
  statsMode: StatsMode;
  setStatsMode: (mode: StatsMode) => void;
  myStatistics: MyStatisticsData | null;
  isLoadingMyStatistics: boolean;
  myStatisticsError: string;
  loadMyStatistics: () => void | Promise<void>;
  loadResultDetail: (resultId: number) => void | Promise<void>;
};

export function StatsScreen({
  statsData,
  statsMode,
  setStatsMode,
  myStatistics,
  isLoadingMyStatistics,
  myStatisticsError,
  loadMyStatistics,
  loadResultDetail,
}: StatsScreenProps) {
  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div className="text-center space-y-2">
        <h2 className="text-4xl font-medium text-gray-800">감성 통계</h2>
        <p className="text-gray-600">
          최근 사용자 흐름과 나의 감성 기록을 확인해보세요.
        </p>
      </div>

      <div className="flex justify-center gap-3">
        <motion.button
          onClick={() => setStatsMode("recent")}
          className="px-6 py-3 font-medium"
          style={{
            background: statsMode === "recent" ? "#FFD93D" : "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow:
              statsMode === "recent"
                ? "4px 4px 0px #2D2D2D"
                : "2px 2px 0px #2D2D2D",
          }}
          whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
          whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
        >
          전체 통계
        </motion.button>

        <motion.button
          onClick={loadMyStatistics}
          className="px-6 py-3 font-medium"
          style={{
            background: statsMode === "me" ? "#A8E6CF" : "#FFFFFF",
            color: "#2D2D2D",
            borderRadius: "25px",
            border: "3px solid #2D2D2D",
            boxShadow:
              statsMode === "me"
                ? "4px 4px 0px #2D2D2D"
                : "2px 2px 0px #2D2D2D",
          }}
          whileHover={{ y: -2, boxShadow: "5px 5px 0px #2D2D2D" }}
          whileTap={{ y: 1, boxShadow: "2px 2px 0px #2D2D2D" }}
        >
          내 통계
        </motion.button>
      </div>

      {statsMode === "recent" && <StatsDisplay data={statsData} />}

      {statsMode === "me" && (
        <div className="space-y-6">
          {isLoadingMyStatistics && (
            <p className="text-center text-gray-700 font-medium">
              내 통계를 불러오는 중이에요...
            </p>
          )}

          {myStatisticsError && (
            <p className="text-center text-red-500 font-medium">
              {myStatisticsError}
            </p>
          )}

          {myStatistics && !isLoadingMyStatistics && (
            <div className="space-y-6">
              <motion.div
                className="p-6 space-y-4"
                style={{
                  background: "#FFFFFF",
                  borderRadius: "30px",
                  border: "4px solid #2D2D2D",
                  boxShadow: "6px 6px 0px #2D2D2D",
                }}
                initial={{ opacity: 0, y: 15 }}
                animate={{ opacity: 1, y: 0 }}
              >
                <div className="space-y-1">
                  <h3 className="text-2xl font-medium text-gray-800">
                    {myStatistics.nickname}님의 감성 기록
                  </h3>
                  <p className="text-gray-600">
                    총 {myStatistics.totalSimulationCount}번 감성 테스트를
                    진행했어요.
                  </p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div
                    className="p-4"
                    style={{
                      background: "#FFF7D6",
                      borderRadius: "20px",
                      border: "3px solid #2D2D2D",
                    }}
                  >
                    <p className="text-sm text-gray-600">
                      가장 많이 나온 날씨
                    </p>
                    <p className="text-xl font-medium text-gray-800">
                      {myStatistics.mostSelectedWeather
                        ? `${myStatistics.mostSelectedWeather.weatherName} (${myStatistics.mostSelectedWeather.count}회)`
                        : "아직 없음"}
                    </p>
                  </div>

                  <div
                    className="p-4"
                    style={{
                      background: "#E5FFF3",
                      borderRadius: "20px",
                      border: "3px solid #2D2D2D",
                    }}
                  >
                    <p className="text-sm text-gray-600">
                      가장 많이 도달한 루트
                    </p>
                    <p className="text-xl font-medium text-gray-800">
                      {myStatistics.mostReachedRoute
                        ? `${myStatistics.mostReachedRoute.routeName} (${myStatistics.mostReachedRoute.count}회)`
                        : "아직 없음"}
                    </p>
                  </div>

                  <div
                    className="p-4"
                    style={{
                      background: "#FFE8EC",
                      borderRadius: "20px",
                      border: "3px solid #2D2D2D",
                    }}
                  >
                    <p className="text-sm text-gray-600">
                      가장 많이 나온 엔딩
                    </p>
                    <p className="text-xl font-medium text-gray-800">
                      {myStatistics.mostReachedEnding
                        ? `${myStatistics.mostReachedEnding.endingName} (${myStatistics.mostReachedEnding.count}회)`
                        : "아직 없음"}
                    </p>
                  </div>
                </div>
              </motion.div>

              <div className="space-y-3">
                <h3 className="text-2xl font-medium text-gray-800">
                  감정별 통계
                </h3>

                <div className="space-y-3">
                  {myStatistics.mainEmotionStats.length === 0 ? (
                    <p className="text-gray-700">아직 감정 통계가 없어요.</p>
                  ) : (
                    myStatistics.mainEmotionStats.map((emotion) => (
                      <div
                        key={emotion.emotion}
                        className="p-4 flex items-center justify-between"
                        style={{
                          background: "#FFFFFF",
                          borderRadius: "20px",
                          border: "3px solid #2D2D2D",
                          boxShadow: "3px 3px 0px #2D2D2D",
                        }}
                      >
                        <span className="text-gray-800 font-medium">
                          {emotion.emotion}
                        </span>
                        <span className="text-gray-600">
                          {emotion.count}회
                        </span>
                      </div>
                    ))
                  )}
                </div>
              </div>

              <div className="space-y-3">
                <h3 className="text-2xl font-medium text-gray-800">
                  최근 감성 결과
                </h3>

                <div className="space-y-3">
                  {myStatistics.recentResults.length === 0 ? (
                    <p className="text-gray-700">최근 결과가 없어요.</p>
                  ) : (
                    myStatistics.recentResults.map((result) => (
                      <motion.div
                        key={result.resultId}
                        onClick={() => loadResultDetail(result.resultId)}
                        className="p-4 cursor-pointer"
                        style={{
                          background: "#FFFFFF",
                          borderRadius: "20px",
                          border: "3px solid #2D2D2D",
                          boxShadow: "3px 3px 0px #2D2D2D",
                        }}
                        whileHover={{
                          y: -3,
                          boxShadow: "5px 5px 0px #2D2D2D",
                        }}
                        whileTap={{
                          y: 1,
                          boxShadow: "2px 2px 0px #2D2D2D",
                        }}
                      >
                        <div className="flex items-center justify-between gap-4">
                          <div>
                            <p className="text-gray-800 font-medium">
                              {result.endingName}
                            </p>
                            <p className="text-sm text-gray-600">
                              {result.weatherName} · {result.routeName}
                            </p>
                          </div>
                          <p className="text-sm text-gray-500">
                            {result.createdAt.replace("T", " ")}
                          </p>
                        </div>
                      </motion.div>
                    ))
                  )}
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}