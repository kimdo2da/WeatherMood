//이 파일은 프론트의 컨트롤러 역할이다 백엔드 api 호출 함수들을 가지고 있음!
// 응답 데이터를 상태로 관리하며 각 화면 컴포넌트에 props 로 전달함
// screen 들이 백엔드 api 호출이 아니라 app.tsx가 백엔드 api 주소를 호출하고 임포트된화면에 주는구조
import { useEffect, useState } from "react";
import { requestApi } from "./api"; //백엔드 api매핑을 이거로 호출
import { SplashScreen } from "./components/SplashScreen";
import { CozyWeatherBackground } from "./components/CozyWeatherBackground";
import { AnimatePresence } from "motion/react";
import { LoginScreen } from "./screens/LoginScreen";
import { SignupScreen } from "./screens/SignupScreen";
import { ContentListScreen } from "./screens/ContentListScreen";
import { StatsScreen } from "./screens/StatsScreen";
import { MyResultsScreen } from "./screens/MyResultsScreen";
import { ResultDetailScreen } from "./screens/ResultDetailScreen";
import { PrescriptionScreen } from "./screens/PrescriptionScreen";
import { DiaryListScreen } from "./screens/DiaryListScreen";
import { SimulationScreen } from "./screens/SimulationScreen";
import { HomeScreen } from "./screens/HomeScreen";
import { AppHeader } from "./components/AppHeader";
import type {
  CardContent,
  ContentListData,
  ContentTab,
  CurrentWeatherData,
  DiaryCreateResponseData,
  DiaryDeleteResponseData,
  DiaryDetailData,
  DiaryItem,
  DiaryListData,
  DiaryMode,
  DiaryUpdateResponseData,
  LoginResponseData,
  MyResultItem,
  MyResultListData,
  MyStatisticsData,
  PopularContentData,
  RecentStatisticsData,
  ResultDetailData,
  Screen,
  SignupResponseData,
  SimulationQuestionData,
  SimulationQuestionView,
  SimulationSubmitResponseData,
  StatsData,
  StatsMode,
  Weather,
} from "./types";

import {
  convertPopularContents,
  convertRecentStatistics,
  convertSimulationQuestions,
  convertWeatherCodeToWeather,
} from "./converters";
import { DiaryWriteScreen } from "./screens/DiaryWriteScreen";
//const data = await requestApi응답타입 매핑주소로 호출함.


const defaultRecommendedContent: CardContent[] = [];

const defaultStatsData: StatsData = {
  totalUsers: 0,
  routes: {
    감성: 0,
    현실도피: 0,
    몰입: 0,
    기분전환: 0,
  },
  topEndings: [
    { name: "아직 통계가 없습니다", count: 0 },
  ],
  popularContent: [
    { title: "아직 인기 콘텐츠가 없습니다", views: 0 },
  ],
};

const defaultSimulationQuestions: SimulationQuestionView[] = [];


export default function App() {
  const [showSplash, setShowSplash] = useState(true);
  const [weather, setWeather] = useState<Weather>("rainy");
  const [currentWeather, setCurrentWeather] = useState<CurrentWeatherData | null>(null);
  const [currentScreen, setCurrentScreen] = useState<Screen>("home");
  const [simulationStep, setSimulationStep] = useState(0);
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");
  const [loginUser, setLoginUser] = useState<LoginResponseData["user"] | null>(null);
  const [loginError, setLoginError] = useState("");
  const [signupEmail, setSignupEmail] = useState("");
  const [signupPassword, setSignupPassword] = useState("");
  const [signupNickname, setSignupNickname] = useState("");
  const [signupError, setSignupError] = useState("");
  const [isSigningUp, setIsSigningUp] = useState(false);
  const [simulationResult, setSimulationResult] =
  useState<SimulationSubmitResponseData | null>(null);
  const [myResults, setMyResults] = useState<MyResultItem[]>([]);
  const [resultDetail, setResultDetail] = useState<ResultDetailData | null>(null);
  const [diaries, setDiaries] = useState<DiaryItem[]>([]);
  const [selectedDiaryResultId, setSelectedDiaryResultId] = useState<number | null>(null);
  const [diaryTitle, setDiaryTitle] = useState("");
  const [diaryContent, setDiaryContent] = useState("");
  const [diaryMode, setDiaryMode] = useState<DiaryMode>("list");
  const [selectedDiary, setSelectedDiary] = useState<DiaryDetailData | null>(null);
  const [isLoadingDiaryDetail, setIsLoadingDiaryDetail] = useState(false);
  const [isUpdatingDiary, setIsUpdatingDiary] = useState(false);
  const [isDeletingDiary, setIsDeletingDiary] = useState(false);
  const [diaryMoodText, setDiaryMoodText] = useState("");
  const [isLoadingDiaries, setIsLoadingDiaries] = useState(false);
  const [isCreatingDiary, setIsCreatingDiary] = useState(false);
  const [diaryError, setDiaryError] = useState("");
  const [isLoadingMyResults, setIsLoadingMyResults] = useState(false);
  const [isLoadingResultDetail, setIsLoadingResultDetail] = useState(false);
  const [myResultsError, setMyResultsError] = useState("");
  const [resultDetailError, setResultDetailError] = useState("");
  const [isSubmittingSimulation, setIsSubmittingSimulation] = useState(false);
  const [simulationError, setSimulationError] = useState("");
  const [recommendedContent, setRecommendedContent] = useState<CardContent[]>(
    defaultRecommendedContent
  );
  const [contentTab, setContentTab] = useState<ContentTab>("anime");
  const [contentItems, setContentItems] = useState<CardContent[]>([]);
  const [contentKeyword, setContentKeyword] = useState("");
  const [isLoadingContents, setIsLoadingContents] = useState(false);
  const [contentError, setContentError] = useState("");
  const [contentPage, setContentPage] = useState(1);
  const [contentHasNext, setContentHasNext] = useState(false);
  const [statsData, setStatsData] = useState<StatsData>(defaultStatsData);
  const [statsMode, setStatsMode] = useState<StatsMode>("recent");
  const [myStatistics, setMyStatistics] = useState<MyStatisticsData | null>(null);
  const [isLoadingMyStatistics, setIsLoadingMyStatistics] = useState(false);
  const [myStatisticsError, setMyStatisticsError] = useState("");
  const [simulationQuestions, setSimulationQuestions] =
  useState<SimulationQuestionView[]>(defaultSimulationQuestions);

const [selectedChoiceIds, setSelectedChoiceIds] = useState<number[]>([]);
//실행시 현재날씨 백엔드 호출 응답 받기 626,652 
useEffect(() => {
  const loadHomeData = async () => {
    try {
      const weatherData = await requestApi<CurrentWeatherData>(
        "/weather/current"
      );

      setCurrentWeather(weatherData);
      setWeather(convertWeatherCodeToWeather(weatherData.weatherCode)); //변환함수 api날씨를 우리식으로
    } catch (error) {
      console.error("현재 날씨 조회 실패:", error);
    }
    try {
      //질문 조회 762
  const questionData = await requestApi<SimulationQuestionData>(
    "/simulation/questions"
  );
  //저장함수
  setSimulationQuestions(convertSimulationQuestions(questionData.questions));
} catch (error) {
  console.error("시뮬레이션 질문 조회 실패:", error);
}
//인기 콘텐츠 가져오기 652,791
    try {
      const popularData = await requestApi<PopularContentData>(
        "/contents/popular"
      );

      setRecommendedContent(convertPopularContents(popularData.items)); //저장되는 상태
    } catch (error) {
      console.error("인기 콘텐츠 조회 실패:", error);
    }

    try {
      //전체 통계 785
      const statisticsData = await requestApi<RecentStatisticsData>(
        "/statistics/recent"
      );
      //저장함수
      setStatsData(convertRecentStatistics(statisticsData));
    } catch (error) {
      console.error("최근 통계 조회 실패:", error);
    }
  };

  loadHomeData();
}, []);


const getWeatherMessage = () => {
  if (currentWeather) {
    return `${currentWeather.city} 현재 날씨는 ${currentWeather.weatherName}, ${currentWeather.temperature.toFixed(
      1
    )}℃예요. 오늘 감정에 어울리는 작품을 찾아볼까요?`;
  }

  const messages = {
    sunny: "화창한 날이에요! 밝은 에너지가 필요한 순간이네요.",
    rainy: "비 오는 날이에요. 차분한 감성이 어울리는 시간이에요.",
    cloudy: "구름 낀 날이에요. 편안한 분위기가 좋겠어요.",
    snowy: "눈 오는 날이에요. 따뜻한 이야기가 필요해요.",
    night: "밤이 깊었네요. 고요한 감성에 빠져보세요.",
  };

  return messages[weather];
};
//회원가입 함수 SignupScreen으로전달 674
const handleSignup = async () => {
  if (!signupEmail.trim() || !signupPassword.trim() || !signupNickname.trim()) {
    setSignupError("이메일, 비밀번호, 닉네임을 모두 입력해주세요.");
    return;
  }

  try {
    setIsSigningUp(true);
    setSignupError("");

    await requestApi<SignupResponseData>("/users/signup", {
      method: "POST",
      body: JSON.stringify({
        email: signupEmail,
        password: signupPassword,
        nickname: signupNickname,
      }),
    });

    setLoginEmail(signupEmail);
    setLoginPassword("");

    setSignupEmail("");
    setSignupPassword("");
    setSignupNickname("");

    setCurrentScreen("login");
  } catch (error) {
    console.error("회원가입 실패:", error);
    setSignupError("회원가입에 실패했어요. 이메일 중복이나 입력값을 확인해주세요.");
  } finally {
    setIsSigningUp(false);
  }
};
//로그인 jwt 저장! 660
const handleLogin = async () => {
  try {
    setLoginError("");

    const data = await requestApi<LoginResponseData>("/users/login", {
      method: "POST",
      body: JSON.stringify({
        email: loginEmail,
        password: loginPassword,
      }),
    });

    localStorage.setItem("accessToken", data.accessToken);
    setLoginUser(data.user);
    setCurrentScreen("home");

    console.log("로그인 성공:", data.user);
  } catch (error) {
    console.error("로그인 실패:", error);
    setLoginError("이메일 또는 비밀번호를 확인해주세요.");
  }
};
//로그아웃 628
const handleLogout = () => {
  localStorage.removeItem("accessToken");
  setLoginUser(null);
  setCurrentScreen("home");
};

const handleSubmitSimulation = async (choiceIds: number[]) => {
  if (!currentWeather) {
    setSimulationError("현재 날씨 정보를 불러오지 못했어요.");
    return;
  }

  if (choiceIds.length === 0) {
    setSimulationError("선택한 답변이 없어요.");
    return;
  }
  

  if (!localStorage.getItem("accessToken")) {
    setSimulationError("시뮬레이션 결과 저장을 위해 로그인이 필요해요.");
    setCurrentScreen("login");
    return;
  }

  try {
    setIsSubmittingSimulation(true);
    setSimulationError("");
    //시뮬레이션 제출 763
    const data = await requestApi<SimulationSubmitResponseData>(
      "/simulation/submit",
      {
        method: "POST",
        body: JSON.stringify({
          weatherCode: currentWeather.weatherCode,
          weatherText: currentWeather.weatherText,
          temperature: currentWeather.temperature,
          selectedChoiceIds: choiceIds,
        }),
      }
    );
    //저장함수들
    console.log("시뮬레이션 결과:", data);
    setSimulationResult(data);
  } catch (error) {
    console.error("시뮬레이션 제출 실패:", error);
    setSimulationError("시뮬레이션 결과 생성에 실패했어요.");
  } finally {
    setIsSubmittingSimulation(false);
  }
};
//결과저장 조회 695
const loadMyResults = async () => {
  if (!localStorage.getItem("accessToken")) {
    setCurrentScreen("login");
    return;
  }

  try {
    setIsLoadingMyResults(true);
    setMyResultsError("");

    const data = await requestApi<MyResultListData>("/users/me/results");
    //저장함수
    setMyResults(data.items);
    setCurrentScreen("myResults");
  } catch (error) {
    console.error("내 결과 목록 조회 실패:", error);
    setMyResultsError("내 결과 목록을 불러오지 못했어요.");
  } finally {
    setIsLoadingMyResults(false);
  }
};
//결과 상세 703
const loadResultDetail = async (resultId: number) => {
  if (!localStorage.getItem("accessToken")) {
    setCurrentScreen("login");
    return;
  }

  try {
    setIsLoadingResultDetail(true);
    setResultDetailError("");

    const data = await requestApi<ResultDetailData>(`/results/${resultId}`);
    //저장함수들
    setResultDetail(data);
    setCurrentScreen("resultDetail");
  } catch (error) {
    console.error("결과 상세 조회 실패:", error);
    setResultDetailError("결과 상세를 불러오지 못했어요.");
  } finally {
    setIsLoadingResultDetail(false);
  }
};
//일기장 713
const loadDiaries = async () => {
  if (!localStorage.getItem("accessToken")) {
    setCurrentScreen("login");
    return;
  }

  try {
    setIsLoadingDiaries(true);
    setDiaryError("");

    const data = await requestApi<DiaryListData>("/diaries");
//저장함수들
setDiaries(data.items);
setSelectedDiary(null);
setDiaryMode("list");
setCurrentScreen("diaryList");
  } catch (error) {
    console.error("일기 목록 조회 실패:", error);
    setDiaryError("일기 목록을 불러오지 못했어요.");
  } finally {
    setIsLoadingDiaries(false);
  }
};
//내 통계 785
const loadMyStatistics = async () => {
  if (!localStorage.getItem("accessToken")) {
    setCurrentScreen("login");
    return;
  }

  try {
    setIsLoadingMyStatistics(true);
    setMyStatisticsError("");

    const data = await requestApi<MyStatisticsData>("/statistics/me");

    setMyStatistics(data);
    setStatsMode("me");
    setCurrentScreen("stats");
  } catch (error) {
    console.error("내 통계 조회 실패:", error);
    setMyStatisticsError("내 통계를 불러오지 못했어요.");
  } finally {
    setIsLoadingMyStatistics(false);
  }
};
//애니 드라마 목록 조회 747
const loadContents = async (
  tab: ContentTab = contentTab,
  page: number = 1
) => {
  try {
    setIsLoadingContents(true);
    setContentError("");

    const safePage = Math.max(page, 1);
    const path =
      tab === "anime"
        ? `/contents/anime?page=${safePage}`
        : `/contents/dramas?page=${safePage}`;

    const data = await requestApi<ContentListData>(path);
    //저장 함수
    setContentTab(tab);
    setContentPage(data.pageInfo.page);
    setContentHasNext(data.pageInfo.hasNext);
    setContentItems(convertPopularContents(data.items));
    setCurrentScreen("contentList");
  } catch (error) {
    console.error("콘텐츠 목록 조회 실패:", error);
    setContentError("콘텐츠 목록을 불러오지 못했어요.");
  } finally {
    setIsLoadingContents(false);
  }
};
//애니 드라마 검색 
const searchContents = async (page: number = 1) => {
  if (!contentKeyword.trim()) {
    await loadContents(contentTab, page);
    return;
  }

   try {
    setIsLoadingContents(true);
    setContentError("");

    const safePage = Math.max(page, 1);
    const encodedKeyword = encodeURIComponent(contentKeyword.trim());

    const path =
      contentTab === "anime"
        ? `/contents/anime/search?keyword=${encodedKeyword}&page=${safePage}`
        : `/contents/dramas/search?keyword=${encodedKeyword}&page=${safePage}`;

    const data = await requestApi<ContentListData>(path);
    //저장함수
    setContentPage(data.pageInfo.page);
    setContentHasNext(data.pageInfo.hasNext);
    setContentItems(convertPopularContents(data.items));
  } catch (error) {
    console.error("콘텐츠 검색 실패:", error);
    setContentError("콘텐츠 검색에 실패했어요.");
  } finally {
    setIsLoadingContents(false);
  }
};

const openDiaryWrite = (resultId: number) => {
  setSelectedDiaryResultId(resultId);
  setDiaryTitle("");
  setDiaryContent("");
  setDiaryMoodText(resultDetail?.mainEmotion || "");
  setDiaryError("");
  setCurrentScreen("diaryWrite");
};
//일기장 작성 737
const handleCreateDiary = async () => {
  if (!selectedDiaryResultId) {
    setDiaryError("연결할 결과가 없어요.");
    return;
  }

  if (!diaryTitle.trim() || !diaryContent.trim()) {
    setDiaryError("제목과 내용을 입력해주세요.");
    return;
  }

  try {
    setIsCreatingDiary(true);
    setDiaryError("");

    await requestApi<DiaryCreateResponseData>("/diaries", {
      method: "POST",
      body: JSON.stringify({
        resultId: selectedDiaryResultId,
        title: diaryTitle,
        content: diaryContent,
        moodText: diaryMoodText,
      }),
    });

    await loadDiaries();
  } catch (error) {
    console.error("일기 작성 실패:", error);
    setDiaryError("일기 작성에 실패했어요.");
  } finally {
    setIsCreatingDiary(false);
  }
};
//일기 상세조회
const loadDiaryDetail = async (diaryId: number) => {
  if (!localStorage.getItem("accessToken")) {
    setCurrentScreen("login");
    return;
  }

  try {
    setIsLoadingDiaryDetail(true);
    setDiaryError("");

    const data = await requestApi<DiaryDetailData>(`/diaries/${diaryId}`);

    setSelectedDiary(data);
    setDiaryMode("detail");
    setCurrentScreen("diaryList");
  } catch (error) {
    console.error("일기 상세 조회 실패:", error);
    setDiaryError("일기 상세를 불러오지 못했어요.");
  } finally {
    setIsLoadingDiaryDetail(false);
  }
};

const openDiaryEdit = () => {
  if (!selectedDiary) {
    setDiaryError("수정할 일기가 없어요.");
    return;
  }

  setDiaryTitle(selectedDiary.title);
  setDiaryContent(selectedDiary.content);
  setDiaryMoodText(selectedDiary.moodText);
  setDiaryError("");
  setDiaryMode("edit");
  setCurrentScreen("diaryList");
};
//일기 수정
const handleUpdateDiary = async () => {
  if (!selectedDiary) {
    setDiaryError("수정할 일기가 없어요.");
    return;
  }

  if (!diaryTitle.trim() || !diaryContent.trim()) {
    setDiaryError("제목과 내용을 입력해주세요.");
    return;
  }

  try {
    setIsUpdatingDiary(true);
    setDiaryError("");

    const data = await requestApi<DiaryUpdateResponseData>(
      `/diaries/${selectedDiary.diaryId}`,
      {
        method: "PUT",
        body: JSON.stringify({
          title: diaryTitle,
          content: diaryContent,
          moodText: diaryMoodText,
        }),
      }
    );

    setSelectedDiary({
      ...selectedDiary,
      title: data.title,
      content: data.content,
      moodText: data.moodText,
      createdAt: data.createdAt,
      updatedAt: data.updatedAt,
    });

    setDiaryMode("detail");
  } catch (error) {
    console.error("일기 수정 실패:", error);
    setDiaryError("일기 수정에 실패했어요.");
  } finally {
    setIsUpdatingDiary(false);
  }
};
//일기 삭제
const handleDeleteDiary = async () => {
  if (!selectedDiary) {
    setDiaryError("삭제할 일기가 없어요.");
    return;
  }

  const confirmed = window.confirm("정말 이 일기를 삭제할까요?");
  if (!confirmed) return;

  try {
    setIsDeletingDiary(true);
    setDiaryError("");

    await requestApi<DiaryDeleteResponseData>(
      `/diaries/${selectedDiary.diaryId}`,
      {
        method: "DELETE",
      }
    );

    setSelectedDiary(null);
    setDiaryMode("list");
    await loadDiaries();
  } catch (error) {
    console.error("일기 삭제 실패:", error);
    setDiaryError("일기 삭제에 실패했어요.");
  } finally {
    setIsDeletingDiary(false);
  }
};
  return (
    <>
      <AnimatePresence mode="wait">
        {showSplash && (
          <SplashScreen key="splash" onEnter={() => setShowSplash(false)} />
        )}
      </AnimatePresence>

      {!showSplash && (
        <div
          className="size-full relative overflow-auto"
          style={{ fontFamily: "'Noto Sans KR', sans-serif" }}
        >
          <CozyWeatherBackground weather={weather} />

<AppHeader
  weather={weather}
  setWeather={setWeather}
  currentScreen={currentScreen}
  goHome={() => setCurrentScreen("home")}
  startSimulation={() => {
    setCurrentScreen("simulation");
    setSimulationStep(0);
    setSelectedChoiceIds([]);
    setSimulationResult(null);
    setSimulationError("");
  }}
  openStats={() => {
    setStatsMode("recent");
    setCurrentScreen("stats");
  }}
  loadMyResults={loadMyResults}
  loadDiaries={loadDiaries}
  isLoggedIn={!!loginUser || !!localStorage.getItem("accessToken")}
  handleLogout={handleLogout}
  openLogin={() => setCurrentScreen("login")}
/>
<main className="max-w-7xl mx-auto px-6 py-12">
{currentScreen === "home" && (
  <HomeScreen
    weatherMessage={getWeatherMessage()}
    recommendedContent={recommendedContent}
    openPrescription={() => setCurrentScreen("prescription")}
    openContents={() => loadContents("anime")}
  />
)}
{currentScreen === "login" && (
  <LoginScreen
    loginEmail={loginEmail}
    setLoginEmail={setLoginEmail}
    loginPassword={loginPassword}
    setLoginPassword={setLoginPassword}
    loginError={loginError}
    handleLogin={handleLogin}
    openSignup={() => {
      setSignupError("");
      setCurrentScreen("signup");
    }}
  />
)}
{currentScreen === "signup" && (
  <SignupScreen
    signupEmail={signupEmail}
    setSignupEmail={setSignupEmail}
    signupPassword={signupPassword}
    setSignupPassword={setSignupPassword}
    signupNickname={signupNickname}
    setSignupNickname={setSignupNickname}
    signupError={signupError}
    isSigningUp={isSigningUp}
    handleSignup={handleSignup}
    openLogin={() => {
      setSignupError("");
      setCurrentScreen("login");
    }}
  />
)}
{currentScreen === "myResults" && (
  <MyResultsScreen
    myResults={myResults}
    isLoadingMyResults={isLoadingMyResults}
    myResultsError={myResultsError}
    loadResultDetail={loadResultDetail}
  />
)}
{currentScreen === "resultDetail" && (
  <ResultDetailScreen
    resultDetail={resultDetail}
    isLoadingResultDetail={isLoadingResultDetail}
    resultDetailError={resultDetailError}
    loadMyResults={loadMyResults}
    openDiaryWrite={openDiaryWrite}
  />
)}
{currentScreen === "diaryList" && (
  <DiaryListScreen
    diaryMode={diaryMode}
    diaries={diaries}
    selectedDiary={selectedDiary}
    isLoadingDiaries={isLoadingDiaries}
    isLoadingDiaryDetail={isLoadingDiaryDetail}
    isUpdatingDiary={isUpdatingDiary}
    isDeletingDiary={isDeletingDiary}
    diaryError={diaryError}
    diaryTitle={diaryTitle}
    setDiaryTitle={setDiaryTitle}
    diaryMoodText={diaryMoodText}
    setDiaryMoodText={setDiaryMoodText}
    diaryContent={diaryContent}
    setDiaryContent={setDiaryContent}
    setDiaryMode={setDiaryMode}
    setSelectedDiary={setSelectedDiary}
    loadDiaryDetail={loadDiaryDetail}
    openDiaryEdit={openDiaryEdit}
    handleUpdateDiary={handleUpdateDiary}
    handleDeleteDiary={handleDeleteDiary}
  />
)}
{currentScreen === "diaryWrite" && (
  <DiaryWriteScreen
    diaryTitle={diaryTitle}
    setDiaryTitle={setDiaryTitle}
    diaryMoodText={diaryMoodText}
    setDiaryMoodText={setDiaryMoodText}
    diaryContent={diaryContent}
    setDiaryContent={setDiaryContent}
    diaryError={diaryError}
    isCreatingDiary={isCreatingDiary}
    handleCreateDiary={handleCreateDiary}
    onCancel={() => setCurrentScreen("resultDetail")}
  />
)}
{currentScreen === "contentList" && (
  <ContentListScreen
    contentTab={contentTab}
    contentItems={contentItems}
    contentKeyword={contentKeyword}
    setContentKeyword={setContentKeyword}
    isLoadingContents={isLoadingContents}
    contentError={contentError}
    contentPage={contentPage}
    contentHasNext={contentHasNext}
    loadContents={loadContents}
    searchContents={searchContents}
  />
)}
{currentScreen === "simulation" && (
  <SimulationScreen
    simulationQuestions={simulationQuestions}
    simulationStep={simulationStep}
    setSimulationStep={setSimulationStep}
    setSelectedChoiceIds={setSelectedChoiceIds}
    handleSubmitSimulation={handleSubmitSimulation}
    simulationResult={simulationResult}
    isSubmittingSimulation={isSubmittingSimulation}
    simulationError={simulationError}
    resetSimulation={() => {
      setSimulationStep(0);
      setSelectedChoiceIds([]);
      setSimulationResult(null);
      setSimulationError("");
      setCurrentScreen("home");
    }}
  />
)}
{currentScreen === "stats" && (
  <StatsScreen
    statsData={statsData}
    statsMode={statsMode}
    setStatsMode={setStatsMode}
    myStatistics={myStatistics}
    isLoadingMyStatistics={isLoadingMyStatistics}
    myStatisticsError={myStatisticsError}
    loadMyStatistics={loadMyStatistics}
    loadResultDetail={loadResultDetail}
  />
)}
{currentScreen === "prescription" && (
  <PrescriptionScreen recommendedContent={recommendedContent} /> 
)}

          </main>
        </div>
      )}
    </>
  );
}