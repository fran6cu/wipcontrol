import { createApp } from "vue";
import { createRouter, createWebHashHistory } from "vue-router";
import "../assets/scss/main.scss";
import App from "../App.vue";
import Index from "../views/Index.vue";
import VueSplide from "@splidejs/vue-splide";
import StoryDetails from "../components/Story/StoryDetails.vue";
import SprintDetails from "../components/Sprint/SprintDetails.vue";
import TaskDetails from "../components/Task/TaskDetails.vue";
import Backlog from "../views/Backlog.vue";
import Sprint from "../views/Sprint.vue";
import Tasks from "../components/Task/TaskStory.vue";
import { fetchUser } from "../store/user.js";

// Vuetify
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: "dark",
  },
});


// routes definition
const routes = [
  {
    path: "/",
    name: "Home",
    component: Index,
    meta: {
      title: "WipControl - Home",
    },
  },
  {
    path: "/story/:id",
    name: "Story Details",
    component: StoryDetails,
    meta: {
      title: "WipControl - Story Details",
    },
  },
  {
    path: "/sprint/:id",
    name: "Sprint Details",
    component: SprintDetails,
    meta: {
      title: "WipControl - Sprint Details",
    },
  },
  {
    path: "/sprint",
    name: "Sprint",
    component: Sprint,
    meta: {
      title: "Wipcontrol Backlog",
    },
  },

  {
    path: "/task/:id",
    name: "Task Details",
    component: TaskDetails,
    meta: {
      title: "WipControl - Task Details",
    },
  },
  {
    path: "/tasks",
    name: "Tasks",
    component: Tasks,
    meta: {
      title: "Wipcontrol Tasks",
    },
  },
  
  
  {
    path: "/backlog",
    name: "Backlog",
    component: Backlog,
    meta: {
      title: "Wipcontrol Backlog",
    },
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/",
  },
];

// router
const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

// title definition
router.beforeEach((to, from) => {
  document.title = to.meta?.title ?? "WipControl";
});

const app = createApp(App);

// dependencies
app.use(router);
app.use(VueSplide);
app.use(vuetify);

// cookies
app.config.globalProperties.$fetchUserOnStart = async () => {
  const cookies = document.cookie.split(";");
  let userID;
  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim();
    const [cookieKey, cookieValue] = cookie.split("=");
    if (cookieKey === "loggedUser") {
      userID = decodeURIComponent(cookieValue);
      break;
    }
  }
  if (userID) {
    await fetchUser(userID);
  }
};

// check user from cookies

app.config.globalProperties.$fetchUserOnStart().then(() => {
  app.mount("#app");
});
