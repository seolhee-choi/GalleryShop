import { createRouter, createWebHistory } from "vue-router";
import { useAccountStore } from "@/scripts/useAccountStore.js";

const routes = [
  {
    path: "/access-denied",
    name: "AccessDenied",
    component: () => import("@/components/user/AccessDenied.vue"),
  },
  {
    path: "/",
    component: () => import("@/UserApp.vue"),
    children: [
      {
        path: "",
        component: () => import("@/pages/user/Home.vue"),
      },
      {
        path: "login",
        component: () => import("@/pages/user/Login.vue"),
        meta: { guestOnly: true },
      },
      {
        path: "cart",
        component: () => import("@/pages/user/Cart.vue"),
      },
      {
        path: "order",
        component: () => import("@/pages/user/Order.vue"),
      },
      {
        path: "orders",
        component: () => import("@/pages/user/Orders.vue"),
      },
      {
        path: "join",
        component: () => import("@/pages/user/Join.vue"),
        meta: { guestOnly: true },
      },
      {
        path: "mypage",
        component: () => import("@/pages/user/Mypage.vue"),
      },
      // {
      //     path: 'data',
      //     component: () => import("@/pages/user/Data.vue"),
      // },
    ],
  },
  {
    path: "/admin",
    component: () => import("@/AdminApp.vue"),
    children: [
      {
        path: "dashboard",
        component: () => import("@/pages/admin/Dashboard.vue"),
      },
      {
        path: "member",
        component: () => import("@/pages/admin/MemberManage.vue"),
      },
      {
        path: "order",
        component: () => import("@/pages/admin/OrderManage.vue"),
      },
      {
        path: "product",
        component: () => import("@/pages/admin/ProductManage.vue"),
      },
      {
        path: "statistics",
        component: () => import("@/pages/admin/Statistics.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

router.beforeEach(async (to, from, next) => {
  // const publicPaths = ["/", "/login", "/join", "/access-denied"];

  const accountStore = useAccountStore();

  // 먼저 사용자 상태를 최신화
  await accountStore.check();

  if (to.meta.requiresAuth && !accountStore.isLoggedIn) {
    return next("/login");
  }

  if (to.meta.guestOnly && accountStore.isLoggedIn) {
    return next("/"); // 로그인된 사용자는 guestOnly 페이지 접근 금지
  }

  next();
});
export default router;
