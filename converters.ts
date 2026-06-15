// 백엔드 api 응답을 프론트에서 쓰기 좋게 변환
//날씨코드,통계데이터,시뮬레이션 질문 ,콘텐츠 카드데이터 등이 있음
import type {
  BackendSimulationQuestion,
  CardContent,
  PopularContentItem,
  RecentStatisticsData,
  RecommendationItem,
  SimulationQuestionView,
  StatsData,
  Weather,
} from "./types";

export function convertPopularContents(items: PopularContentItem[]): CardContent[] {
  return items.map((item) => ({
    title: item.title,
    type: item.contentType === "ANIME" ? "anime" : "drama",
    thumbnail: item.posterUrl || undefined,
    tags: item.genre
      ? item.genre
          .split(",")
          .map((tag) => tag.trim())
          .filter(Boolean)
          .slice(0, 2)
      : [item.contentType === "ANIME" ? "애니" : "드라마"],
    emotionScore: {
      감성: Math.min(Math.round((item.score || 7) * 10), 100),
      현실도피: 70,
      몰입: 80,
    },
  }));
}

export function convertRecentStatistics(data: RecentStatisticsData): StatsData {
  const findPercentage = (routeName: string) => {
    return (
      data.routeStats.find((route) => route.routeName === routeName)
        ?.percentage || 0
    );
  };

  const totalUsers = data.routeStats.reduce(
    (sum, route) => sum + route.count,
    0
  );

  return {
    totalUsers,
    routes: {
      감성: findPercentage("감성 루트"),
      현실도피: findPercentage("현실도피 루트"),
      몰입: findPercentage("몰입 루트"),
      기분전환: findPercentage("기분전환 루트"),
    },
    topEndings:
      data.popularEndings.length > 0
        ? data.popularEndings.map((ending) => ({
            name: ending.endingName,
            count: ending.count,
          }))
        : [{ name: "아직 통계가 없습니다", count: 0 }],
    popularContent:
      data.popularContents.length > 0
        ? data.popularContents.map((content) => ({
            title: content.title,
            views: content.count,
          }))
        : [{ title: "아직 인기 콘텐츠가 없습니다", views: 0 }],
  };
}

export function convertWeatherCodeToWeather(weatherCode: string): Weather {
  switch (weatherCode) {
    case "CLEAR":
      return "sunny";
    case "RAIN":
      return "rainy";
    case "CLOUDS":
      return "cloudy";
    case "SNOW":
      return "snowy";
    case "NIGHT":
      return "night";
    default:
      return "cloudy";
  }
}

function getChoiceIcon(choiceIndex: number) {
  const icons = ["lonely", "hard", "calm", "good"];
  return icons[choiceIndex] || "calm";
}

export function convertSimulationQuestions(
  questions: BackendSimulationQuestion[]
): SimulationQuestionView[] {
  return questions.map((question) => ({
    question: question.questionText,
    options: question.choices.map((choice, index) => ({
      id: String(choice.choiceId),
      text: choice.choiceText,
      icon: getChoiceIcon(index),
      emotions: {
        감성: 0,
        현실도피: 0,
        몰입: 0,
        기분전환: 0,
      },
    })),
  }));
}

export function convertRecommendationsToCards(
  items: RecommendationItem[]
): CardContent[] {
  return items.map((item) => ({
    title: item.title,
    type: item.contentType === "ANIME" ? "anime" : "drama",
    thumbnail: item.posterUrl || undefined,
    tags: item.genre
      ? item.genre
          .split(",")
          .map((tag) => tag.trim())
          .filter(Boolean)
          .slice(0, 2)
      : [item.contentType === "ANIME" ? "애니" : "드라마"],
    emotionScore: {
      감성: item.matchScore,
      현실도피: Math.max(item.matchScore - 10, 0),
      몰입: Math.min(item.matchScore + 5, 100),
    },
  }));
}