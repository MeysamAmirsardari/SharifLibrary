public abstract class Store {
    public static void sellBook (Book book, String memberType, String ID,String time, double price){
        if (MainLib.searchInMembers(ID,memberType)==1){
            Employee worker = MainLib.findWorker(time,Main.toDay.weekDay);
            if (memberType.equalsIgnoreCase("Student")){
                Student student = Main.searchStudent(ID);
                if ((student.budget-price)>-10000){
                    student.budget -= price;
                    MainLib.shopList.get(MainLib.shopList.indexOf(book)).stockInStore--;
                    String result = book.bookName+","+book.ISBN+","+book.publishedYear+","+memberType+
                            ","+ID+","+Main.toDay.date+","+Integer.toString((int) (price))+","+worker.firstName;
                    MainLib.soldBooks.add(result);
                } else
                    System.out.println("Not enough budget!");
            } else {
                Professor professor = Main.searchProfessor(ID);
                if ((professor.budget-price)>-10000){
                    professor.budget -= price;
                    MainLib.shopList.get(MainLib.shopList.indexOf(book)).stockInStore--;
                    String result = book.bookName+","+book.ISBN+","+book.publishedYear+","+memberType+
                            ","+ID+","+Main.toDay.date+","+Integer.toString((int) price)+","+worker.firstName;
                    MainLib.soldBooks.add(result);
                } else
                    System.out.println("Not enough budget!");
            }
        } else
            System.out.println("Member not found!");
    }


    public static int giveBackToStore (String bookDetails, String memberType, String ID, String time){
        String[] details = bookDetails.split(",");
        Book book = Main.searchInBookList(details[0],details[1],details[2]);
        if ((!(book.equals(null)))&&(MainLib.shopList.contains(book))) {
            int index = MainLib.searchInSoldBooks(book);
            if (ID.equals(MainLib.soldBooks.get(index).split(",")[4])){
                MainLib.shopList.get(MainLib.shopList.indexOf(book)).stockInStore++;
                String sellDay = MainLib.soldBooks.get(index).split(",")[5];
                int sellPrice = Integer.parseInt(MainLib.soldBooks.get(index).split(",")[6]);
                Date sellDate = new Date(sellDay);
                double GBPrice = (100 - ((Date.deltaTime(sellDate, Main.toDay)) * 10) * sellPrice) / 100;
                MainLib.soldBooks.remove(index);
                Employee employee = MainLib.findWorker(time, Main.toDay.weekDay);
                String GBTSDetails = bookDetails + "," + memberType + "," + ID + "," + sellDay + "," + Main.toDay.date + ","
                        + Integer.toString((int) GBPrice) + employee.firstName;
                MainLib.storeReimbursedBooks.add(GBTSDetails);
                return 1;
            }
        }
        return 0;
    }


    public int checkDiscountCode(String code){
        if (code.equals(MainLib.discountCode))
            return 1;
        return 0;
    }
}
