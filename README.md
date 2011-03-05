## sothis ##

a rails like java mvc framework

code example:

	public class HelloController {
		private String message;

		public void sayAction(HomePageModel model,
				@Parameter(name = "myFile") List<File> someFiles,
				@Parameter(name = "date", pattern = "yyyy-MM-dd") Date date) {
			System.out.println(model.getMyFile());
			System.out.println(model.getMyFileContentType());
			System.out.println(model.getMyFileFileName());
			System.out.println(model.getMessage());
			System.out.println(model.getStatus());
			System.out.println(message);
			for (File f : someFiles) {
				System.out.println("someFiles:" + f);
			}
			System.out.println("date:" + date);
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

will match request '/hello/say?date=2011-03-06'