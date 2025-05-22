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
        meta: { requiresAuth: true },
      },
      {
        path: "order",
        component: () => import("@/pages/user/Order.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "orders",
        component: () => import("@/pages/user/Orders.vue"),
        meta: { requiresAuth: true },
      },
      {
        path: "join",
        component: () => import("@/pages/user/Join.vue"),
        meta: { guestOnly: true },
      },
      {
        path: "mypage",
        component: () => import("@/pages/user/Mypage.vue"),
        meta: { requiresAuth: true },
      },
      // {
      //     path: 'data',
      //     component: () => import("@/pages/user/BulkDataModal.vue"),
      // },
    ],
  },
  {
    path: "/admin",
    component: () => import("@/AdminApp.vue"),
    children: [
      {
        path: "",
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
        path: "review",
        component: () => import("@/pages/admin/ReviewManage.vue"),
      },
      {
        path: "statistics",
        component: () => import("@/pages/admin/Statistics.vue"),
      },
    ],
    meta: { requiresAdmin: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

router.beforeEach(async (to, from, next) => {
  console.log("Navigating to:", to.fullPath);
  console.log("Requires auth:", to.meta.requiresAuth);
  const accountStore = useAccountStore();

  await accountStore.check();

  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
  const guestOnly = to.matched.some((record) => record.meta.guestOnly);
  const requiresAdmin = to.matched.some((record) => record.meta.requiresAdmin);

  if (requiresAuth && !accountStore.isLoggedIn) {
    return next("/login");
  }

  if (guestOnly && accountStore.isLoggedIn) {
    return next("/"); // 로그인된 사용자는 guestOnly 페이지 접근 금지
  }

  // admin 권한이 필요한 페이지 체크
  if (requiresAdmin) {
    if (accountStore.account.role === "ADMIN") {
      next();
    } else {
      return next("/login"); // 또는 권한 없다는 페이지로 리다이렉트
    }
  }

  next();
});
export default router;
