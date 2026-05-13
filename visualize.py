"""
실행 방법:
    pip install matplotlib requests
    python visualize.py
"""

import os
import sys

import matplotlib.pyplot as plt
import requests

API_URL = "http://localhost:8080/api/events/analytics"
CHARTS_DIR = "charts"


def fetch_analytics():
    try:
        resp = requests.get(API_URL, timeout=10)
        resp.raise_for_status()
    except requests.RequestException as e:
        print(f"API 요청 실패: {e}")
        sys.exit(1)

    body = resp.json()
    if not body.get("isSuccess"):
        print(f"API 응답 오류: {body.get('message')}")
        sys.exit(1)

    return body["result"]


def chart_event_type_counts(ax, data):
    """이벤트 타입별 발생 횟수 — 막대 차트."""
    items = data["eventTypeCounts"]
    types = [item["eventType"] for item in items]
    counts = [item["count"] for item in items]

    bars = ax.bar(types, counts, color=["#4C72B0", "#55A868", "#C44E52", "#8172B2"])
    ax.set_title("Event Count by Type", fontsize=13, fontweight="bold")
    ax.set_xlabel("Event Type")
    ax.set_ylabel("Count")
    ax.tick_params(axis="x", rotation=20)

    for bar, count in zip(bars, counts):
        ax.text(bar.get_x() + bar.get_width() / 2, bar.get_height(),
                str(count), ha="center", va="bottom", fontsize=9)


def chart_user_event_counts(ax, data):
    """유저별 총 이벤트 수 — 막대 차트."""
    items = data["userEventCounts"]
    users = [item["userId"] for item in items]
    counts = [item["count"] for item in items]

    bars = ax.bar(users, counts, color="#4C72B0")
    ax.set_title("Event Count by User", fontsize=13, fontweight="bold")
    ax.set_xlabel("User ID")
    ax.set_ylabel("Count")
    ax.tick_params(axis="x", rotation=20)

    for bar, count in zip(bars, counts):
        ax.text(bar.get_x() + bar.get_width() / 2, bar.get_height(),
                str(count), ha="center", va="bottom", fontsize=9)


def chart_error_ratio(ax, data):
    """에러 이벤트 비율 — 파이 차트."""
    ratio = data["errorEventRatio"]
    sizes = [ratio, 1 - ratio]
    labels = ["ERROR", "Others"]
    colors = ["#C44E52", "#55A868"]

    wedges, texts, autotexts = ax.pie(
        sizes, labels=labels, colors=colors, autopct="%1.1f%%",
        startangle=90, textprops={"fontsize": 10},
    )
    for t in autotexts:
        t.set_fontweight("bold")
    ax.set_title("Error Event Ratio", fontsize=13, fontweight="bold")


def chart_error_message_counts(ax, data):
    """에러 메시지별 발생 빈도 — 막대 차트."""
    items = data["errorMessageCounts"]
    messages = [item["errorMessage"] for item in items]
    counts = [item["count"] for item in items]

    # 긴 메시지 줄바꿈 처리
    wrapped = [m if len(m) <= 25 else m[:22] + "..." for m in messages]

    bars = ax.barh(wrapped, counts, color="#8172B2")
    ax.set_title("Error Message Frequency", fontsize=13, fontweight="bold")
    ax.set_xlabel("Count")

    for bar, count in zip(bars, counts):
        ax.text(bar.get_width(), bar.get_y() + bar.get_height() / 2,
                f" {count}", va="center", fontsize=9)



def save_summary_chart(data):
    fig, axes = plt.subplots(2, 2, figsize=(14, 10))

    chart_event_type_counts(axes[0, 0], data)
    chart_user_event_counts(axes[0, 1], data)
    chart_error_ratio(axes[1, 0], data)
    chart_error_message_counts(axes[1, 1], data)

    fig.suptitle("Event Pipeline Analytics Summary", fontsize=16, fontweight="bold")
    fig.tight_layout(rect=[0, 0, 1, 0.95])

    path = os.path.join(CHARTS_DIR, "analytics_summary.png")
    fig.savefig(path, dpi=150)
    plt.close(fig)
    print(f"  saved: {path}")


def main():
    os.makedirs(CHARTS_DIR, exist_ok=True)

    print("Fetching analytics data from API...")
    data = fetch_analytics()

    print(f"\n--- Analytics Data ---")
    print(f"Event type counts : {data['eventTypeCounts']}")
    print(f"User event counts : {data['userEventCounts']}")
    print(f"Error event ratio : {data['errorEventRatio']}")
    print(f"Error msg counts  : {data['errorMessageCounts']}")

    print(f"\nGenerating summary chart...")
    save_summary_chart(data)

    print(f"\nDone. Chart saved to {CHARTS_DIR}/analytics_summary.png")


if __name__ == "__main__":
    main()
