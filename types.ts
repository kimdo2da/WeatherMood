//백엔드 응답 데이터 타입 정의
// 프론트 화면에서 사용하는 데이터들의 타입을 정의함
export type Weather = "sunny" | "rainy" | "cloudy" | "snowy" | "night";

export type Screen =
  | "home"
  | "simulation"
  | "stats"
  | "prescription"
  | "login"
  | "signup"
  | "myResults"
  | "resultDetail"
  | "diaryList"
  | "diaryWrite"
  | "contentList";

export type DiaryMode = "list" | "detail" | "edit";

export type PopularContentItem = {
  externalApiId: string;
  title: string;
  contentType: "ANIME" | "DRAMA";
  genre: string;
  description: string;
  posterUrl: string | null;
  score: number | null;
};

export type PopularContentData = {
  items: PopularContentItem[];
};

export type ContentTab = "anime" | "drama";

export type ContentListData = {
  items: PopularContentItem[];
  pageInfo: {
    page: number;
    hasNext: boolean;
  };
};

export type RecentStatisticsData = {
  period: string;
  routeStats: {
    routeName: string;
    count: number;
    percentage: number;
  }[];
  popularEndings: {
    rank: number;
    endingName: string;
    count: number;
  }[];
  popularContents: {
    rank: number;
    title: string;
    count: number;
  }[];
  trend: Record<string, string>;
};

export type MyStatisticsData = {
  nickname: string;
  totalSimulationCount: number;
  mostSelectedWeather: {
    weatherName: string;
    count: number;
  } | null;
  mostReachedRoute: {
    routeName: string;
    count: number;
  } | null;
  mostReachedEnding: {
    endingName: string;
    count: number;
  } | null;
  mainEmotionStats: {
    emotion: string;
    count: number;
  }[];
  recentResults: {
    resultId: number;
    weatherName: string;
    routeName: string;
    endingName: string;
    createdAt: string;
  }[];
};

export type StatsMode = "recent" | "me";

export type CurrentWeatherData = {
  weatherCode: string;
  weatherName: string;
  weatherText: string;
  temperature: number;
  city: string;
};

export type LoginResponseData = {
  accessToken: string;
  tokenType: string;
  user: {
    userId: number;
    email: string;
    nickname: string;
  };
};

export type SignupResponseData = {
  userId: number;
  email: string;
  nickname: string;
};

export type MyResultListData = {
  items: MyResultItem[];
};

export type MyResultItem = {
  resultId: number;
  weatherName: string;
  routeName: string;
  endingName: string;
  mainEmotion: string;
  totalScore: number;
  createdAt: string;
};

export type ResultDetailData = {
  resultId: number;
  userId: number;
  weather: {
    weatherId: number;
    weatherCode: string;
    weatherName: string;
    weatherText: string;
    temperature: number;
  };
  route: {
    routeId: number;
    routeName: string;
  };
  mainEmotion: string;
  totalScore: number;
  ending: {
    endingId: number;
    endingName: string;
    description: string;
  };
  recommendations: {
    prescription: RecommendationItem[];
    reverse: RecommendationItem[];
    set: {
      anime?: RecommendationItem;
      drama?: RecommendationItem;
    };
  };
  createdAt: string;
};

export type DiaryListData = {
  items: DiaryItem[];
};

export type DiaryItem = {
  diaryId: number;
  resultId: number;
  title: string;
  moodText: string;
  routeName: string;
  endingName: string;
  createdAt: string;
};

export type DiaryCreateResponseData = {
  diaryId: number;
  resultId: number;
  title: string;
  content: string;
  moodText: string;
  createdAt: string;
  updatedAt: string;
};

export type DiaryDetailData = {
  diaryId: number;
  resultId: number;
  title: string;
  content: string;
  moodText: string;
  resultSummary: {
    weatherName: string;
    routeName: string;
    endingName: string;
    mainEmotion: string;
  };
  createdAt: string;
  updatedAt: string;
};

export type DiaryUpdateResponseData = {
  diaryId: number;
  resultId: number;
  title: string;
  content: string;
  moodText: string;
  createdAt: string;
  updatedAt: string;
};

export type DiaryDeleteResponseData = {
  diaryId: number;
};

export type SimulationSubmitResponseData = {
  resultId: number;
  weather: {
    weatherCode: string;
    weatherName: string;
    weatherText: string;
    temperature: number;
  };
  route: {
    routeId: number;
    routeName: string;
    totalScore: number;
  };
  mainEmotion: string;
  ending: {
    endingId: number;
    endingName: string;
    description: string;
  };
  recommendations: {
    prescription: RecommendationItem[];
    reverse: RecommendationItem[];
    set: {
      anime?: RecommendationItem;
      drama?: RecommendationItem;
    };
  };
};

export type RecommendationItem = {
  contentId: number | null;
  externalApiId?: string | null;
  title: string;
  contentType: "ANIME" | "DRAMA";
  genre: string;
  matchScore: number;
  reason: string;
  posterUrl: string | null;
};

export type SimulationQuestionData = {
  questions: BackendSimulationQuestion[];
};

export type BackendSimulationQuestion = {
  questionId: number;
  questionText: string;
  questionOrder: number;
  choices: BackendSimulationChoice[];
};

export type BackendSimulationChoice = {
  choiceId: number;
  choiceText: string;
  choiceOrder: number;
};

export type SimulationOption = {
  id: string;
  text: string;
  icon: string;
  emotions: {
    감성: number;
    현실도피: number;
    몰입: number;
    기분전환: number;
  };
};

export type SimulationQuestionView = {
  question: string;
  options: SimulationOption[];
};

export type CardContent = {
  title: string;
  type: "drama" | "anime";
  thumbnail?: string;
  tags: string[];
  emotionScore: {
    감성: number;
    현실도피: number;
    몰입: number;
  };
};

export type StatsData = {
  totalUsers: number;
  routes: {
    감성: number;
    현실도피: number;
    몰입: number;
    기분전환: number;
  };
  topEndings: Array<{
    name: string;
    count: number;
  }>;
  popularContent: Array<{
    title: string;
    views: number;
  }>;
};