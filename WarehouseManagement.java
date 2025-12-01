import java.util.Locale;
import java.util.Scanner;

/**
 * Sistem Manajemen Pergudangan (Console)
 * - Struktur data: Item[] (array)
 * - Sorting: Merge Sort (by ID asc, by qty desc)
 * - Searching: Binary Search (rekursif by ID), Linear Search by name
 * - Rekursi: merge sort & binary search
 *
 * Compile: javac Main.java
 * Run    : java Main
 */

class Item {
    String itemID;    // unique ID (e.g., "BRG001")
    String name;
    String category;
    int quantity;
    double price;     // unit price

    public Item(String itemID, String name, String category, int quantity, double price) {
        this.itemID = itemID;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ID      : " + itemID +
               "\nName    : " + name +
               "\nCategory: " + category +
               "\nQty     : " + quantity +
               "\nPrice   : " + price;
    }
}

public class Main {
    static final int MAX = 2000;
    static Item[] items = new Item[MAX];
    static int itemCount = 0;

    static Scanner in = new Scanner(System.in).useLocale(Locale.US);

    public static void main(String[] args) {
        int pilihan;
        do {
            menu();
            pilihan = inputInt("Pilih menu: ");
            switch (pilihan) {
                case 1:
                    addItem();
                    break;
                case 2:
                    displayAll();
                    break;
                case 3:
                    sortByID();
                    break;
                case 4:
                    sortByQuantity();
                    break;
                case 5:
                    searchByIDBinary();
                    break;
                case 6:
                    searchByNameLinear();
                    break;
                case 7:
                    updateStock();
                    break;
                case 0:
                    System.out.println("Keluar. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
            System.out.println();
        } while (pilihan != 0);
    }

    static void menu() {
        System.out.println("===== WAREHOUSE MANAGEMENT SYSTEM =====");
        System.out.println("1. Tambah Item");
        System.out.println("2. Tampilkan Semua Item");
        System.out.println("3. Sorting by ItemID (ascending) [Merge Sort]");
        System.out.println("4. Sorting by Quantity (descending) [Merge Sort]");
        System.out.println("5. Cari Item by ID (Binary Search - rekursif)");
        System.out.println("6. Cari Item by Name (Linear Search)");
        System.out.println("7. Update Stock (In/Out)");
        System.out.println("0. Keluar");
    }

    // ---------- Input Helpers ----------
    static int inputInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = in.nextLine();
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.println("Input harus angka. Coba lagi.");
            }
        }
    }

    static double inputDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = in.nextLine();
                return Double.parseDouble(s.trim());
            } catch (Exception e) {
                System.out.println("Input harus angka desimal. Coba lagi.");
            }
        }
    }

    static String inputString(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    // ---------- Menu Functions ----------
    static void addItem() {
        if (itemCount >= MAX) {
            System.out.println("Kapasitas gudang penuh!");
            return;
        }
        String id = inputString("Masukkan Item ID: ");
        // Optional: check uniqueness
        if (findIndexByID(id) != -1) {
            System.out.println("Item ID sudah ada. Gunakan menu update untuk merubah stok.");
            return;
        }
        String name = inputString("Masukkan Nama Item: ");
        String category = inputString("Masukkan Kategori: ");
        int qty = inputInt("Masukkan Quantity awal: ");
        double price = inputDouble("Masukkan Harga per unit: ");

        items[itemCount++] = new Item(id, name, category, qty, price);
        System.out.println("Item berhasil ditambahkan.");
    }

    static void displayAll() {
        if (itemCount == 0) {
            System.out.println("Belum ada item di gudang.");
            return;
        }
        System.out.println("===== LIST ITEM =====");
        for (int i = 0; i < itemCount; i++) {
            System.out.println("Item ke-" + (i + 1));
            System.out.println(items[i].toString());
            System.out.println("---------------------");
        }
    }

    // ---------- Sorting: Merge Sort ----------
    static void sortByID() {
        if (itemCount <= 1) {
            System.out.println("Data kurang dari 2, tidak perlu diurutkan.");
            return;
        }
        Item[] temp = new Item[itemCount];
        mergeSortByID(items, temp, 0, itemCount - 1);
        System.out.println("Data berhasil diurutkan berdasarkan ItemID (ascending).");
    }

    static void mergeSortByID(Item[] arr, Item[] temp, int left, int right) {
        if (left >= right) return; // basis rekursi
        int mid = (left + right) / 2;
        mergeSortByID(arr, temp, left, mid);
        mergeSortByID(arr, temp, mid + 1, right);
        mergeByID(arr, temp, left, mid, right);
    }

    static void mergeByID(Item[] arr, Item[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) temp[i] = arr[i];
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (temp[i].itemID.compareTo(temp[j].itemID) <= 0) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }
        while (i <= mid) arr[k++] = temp[i++];
        // sisa kanan sudah di tempat
    }

    static void sortByQuantity() {
        if (itemCount <= 1) {
            System.out.println("Data kurang dari 2, tidak perlu diurutkan.");
            return;
        }
        Item[] temp = new Item[itemCount];
        mergeSortByQty(items, temp, 0, itemCount - 1);
        System.out.println("Data berhasil diurutkan berdasarkan Quantity (descending).");
    }

    static void mergeSortByQty(Item[] arr, Item[] temp, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSortByQty(arr, temp, left, mid);
        mergeSortByQty(arr, temp, mid + 1, right);
        mergeByQty(arr, temp, left, mid, right);
    }

    static void mergeByQty(Item[] arr, Item[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) temp[i] = arr[i];
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            // descending quantity
            if (temp[i].quantity >= temp[j].quantity) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }
        while (i <= mid) arr[k++] = temp[i++];
    }

    // ---------- Searching ----------
    // Linear search by name (substring, case-insensitive)
    static void searchByNameLinear() {
        if (itemCount == 0) {
            System.out.println("Belum ada data.");
            return;
        }
        String key = inputString("Masukkan keyword nama: ").toLowerCase();
        boolean found = false;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].name.toLowerCase().contains(key)) {
                if (!found) {
                    System.out.println("Item yang cocok:");
                    found = true;
                }
                System.out.println("---------------------");
                System.out.println(items[i].toString());
            }
        }
        if (!found) System.out.println("Tidak ada item yang cocok.");
    }

    // Binary search by ID (rekursif). Array harus sudah di-sort by ID.
    static void searchByIDBinary() {
        if (itemCount == 0) {
            System.out.println("Belum ada data.");
            return;
        }
        System.out.println("Pastikan data sudah terurut berdasarkan ItemID.");
        String key = inputString("Masukkan ItemID yang dicari: ");
        int idx = binarySearchByID(items, 0, itemCount - 1, key);
        if (idx == -1) {
            System.out.println("Item dengan ID " + key + " tidak ditemukan.");
        } else {
            System.out.println("Item ditemukan pada indeks " + idx + ":");
            System.out.println(items[idx].toString());
        }
    }

    static int binarySearchByID(Item[] arr, int left, int right, String key) {
        if (left > right) return -1;
        int mid = (left + right) / 2;
        int cmp = arr[mid].itemID.compareTo(key);
        if (cmp == 0) return mid;
        else if (cmp > 0) return binarySearchByID(arr, left, mid - 1, key);
        else return binarySearchByID(arr, mid + 1, right, key);
    }

    // Helper to find index by ID (iterative) — used for uniqueness checks
    static int findIndexByID(String id) {
        for (int i = 0; i < itemCount; i++) {
            if (items[i].itemID.equals(id)) return i;
        }
        return -1;
    }

    // ---------- Stock Update ----------
    static void updateStock() {
        if (itemCount == 0) {
            System.out.println("Belum ada data.");
            return;
        }
        String id = inputString("Masukkan ItemID yang akan diupdate: ");
        int idx = findIndexByID(id);
        if (idx == -1) {
            System.out.println("Item tidak ditemukan.");
            return;
        }
        System.out.println("Item ditemukan:");
        System.out.println(items[idx].toString());
        System.out.println("1. Tambah stok (IN)");
        System.out.println("2. Kurangi stok (OUT)");
        int pilih = inputInt("Pilih aksi: ");
        if (pilih == 1) {
            int add = inputInt("Masukkan jumlah masuk: ");
            if (add < 0) { System.out.println("Jumlah tidak valid."); return; }
            items[idx].quantity += add;
            System.out.println("Stok berhasil ditambah. Qty sekarang: " + items[idx].quantity);
        } else if (pilih == 2) {
            int sub = inputInt("Masukkan jumlah keluar: ");
            if (sub < 0 || sub > items[idx].quantity) {
                System.out.println("Jumlah tidak valid atau melebihi stok.");
                return;
            }
            items[idx].quantity -= sub;
            System.out.println("Stok berhasil dikurangi. Qty sekarang: " + items[idx].quantity);
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    // ---------- Wrapper untuk menu calls (tidy) ----------
    static void searchByNameLinearMenu() { searchByNameLinear(); }
    static void searchByIDBinaryMenu() { searchByIDBinary(); }

}
